import {
  createConnection,
  createServer,
  createTypeScriptProject,
  loadTsdkByPath,
} from '@volar/language-server/node'
import type { LanguageServicePlugin } from '@volar/language-service'
import { create as createTypeScriptServices } from 'volar-service-typescript'
import { QinLanguagePlugin } from './QinLanguagePlugin'
import { QinLanguageServicePlugin, provideSourceImplementations } from './QinLanguageServicePlugin'
import { extensionWithoutDot, resolveLanguageServerMetadata } from './LanguageServerMetadata'
import { logToFile } from './logutil'
import { URI } from 'vscode-uri'

const QIN_IDENTIFIER_COMPLETION_TRIGGER_CHARACTERS = [
  ...'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_$',
]

logToFile('=== Qin Language Server Starting ===')
logToFile('Process ID: ' + process.pid)
logToFile('Node version: ' + process.version)
logToFile('Current directory: ' + process.cwd())

const connection = createConnection()
const server = createServer(connection)

connection.listen()

connection.onInitialize((params) => {
  logToFile('=== onInitialize ===')
  logToFile('Client info: ' + JSON.stringify(params.clientInfo))
  logToFile('Root URI: ' + params.rootUri)
  logToFile('Workspace folders: ' + JSON.stringify(params.workspaceFolders))
  logToFile('Initialization options: ' + JSON.stringify(params.initializationOptions))

  const tsdkPath = params.initializationOptions?.typescript?.tsdk ?? process.env.QIN_LSP_TYPESCRIPT_TSDK
  if (!tsdkPath) {
    throw new Error('Qin language server requires initializationOptions.typescript.tsdk or QIN_LSP_TYPESCRIPT_TSDK')
  }
  const languageServerMetadata = resolveLanguageServerMetadata(params.initializationOptions)
  const sourceExtension = extensionWithoutDot(languageServerMetadata.sourceExtension)
  logToFile('Language server metadata: ' + JSON.stringify(languageServerMetadata))

  const tsdk = loadTsdkByPath(tsdkPath, params.locale)
  const languagePlugins = [QinLanguagePlugin(languageServerMetadata)]
  const languageServicePlugins = [
    QinLanguageServicePlugin,
    ...withTypeScriptDeclarationProvider(withoutTypeScriptDocumentSymbols(createTypeScriptServices(tsdk.typescript, {
      disableAutoImportCache: true,
      isValidationEnabled(document) {
        return document.languageId !== 'qin' && !isQinDocumentUri(document.uri, sourceExtension)
      },
    }))).map(plugin => withQinSourceImplementationProvider(plugin)).map(plugin => withCompletionLogging(plugin)),
  ]
  const tsProject = createTypeScriptProject(
    tsdk.typescript,
    tsdk.diagnosticMessages,
    () => ({
      languagePlugins,
    })
  )

  const result = withQinCompletionTriggerCharacters(server.initialize(params, tsProject, [...languageServicePlugins]))
  logToFile('=== Qin Language Server Initialized ===')
  return result
})

connection.onInitialized(() => {
  server.initialized()
})

connection.onShutdown(() => {
  server.shutdown()
})

process.on('uncaughtException', (error) => {
  logToFile('Uncaught exception:', error.stack || error.message)
})

process.on('unhandledRejection', (reason) => {
  logToFile('Unhandled rejection:', String(reason))
})

function isQinDocumentUri(uri: string, sourceExtension: string): boolean {
  const lowerUri = uri.toLowerCase()
  return lowerUri.endsWith(`.${sourceExtension}`)
    || lowerUri.includes(`.${sourceExtension}.`)
    || lowerUri.includes(`.${sourceExtension}%`)
    || lowerUri.includes(`%2e${sourceExtension}`)
    || lowerUri.includes(`%252e${sourceExtension}`)
}

function withQinCompletionTriggerCharacters<T extends { capabilities?: { completionProvider?: { triggerCharacters?: string[] } } }>(result: T): T {
  const completionProvider = result.capabilities?.completionProvider
  if (!completionProvider) {
    return result
  }
  completionProvider.triggerCharacters = [
    ...new Set([
      ...(completionProvider.triggerCharacters ?? []),
      ...QIN_IDENTIFIER_COMPLETION_TRIGGER_CHARACTERS,
    ]),
  ]
  logToFile('Completion trigger characters:', JSON.stringify(completionProvider.triggerCharacters))
  return result
}

function withoutTypeScriptDocumentSymbols(plugins: LanguageServicePlugin[]): LanguageServicePlugin[] {
  return plugins.map(plugin => {
    if (plugin.name !== 'typescript-syntactic') {
      return plugin
    }
    return {
      ...plugin,
      capabilities: {
        ...plugin.capabilities,
        documentSymbolProvider: false,
      },
      create(context) {
        const service = plugin.create(context)
        return {
          ...service,
          provideDocumentSymbols: undefined,
        }
      },
    }
  })
}

function withTypeScriptDeclarationProvider(plugins: LanguageServicePlugin[]): LanguageServicePlugin[] {
  return plugins.map(plugin => {
    if (!plugin.capabilities.definitionProvider || plugin.capabilities.declarationProvider) {
      return plugin
    }
    return {
      ...plugin,
      capabilities: {
        ...plugin.capabilities,
        declarationProvider: true,
      },
      create(context) {
        const service = plugin.create(context)
        return {
          ...service,
          provideDeclaration: service.provideDefinition,
        }
      },
    }
  })
}

function withQinSourceImplementationProvider(plugin: LanguageServicePlugin): LanguageServicePlugin {
  return {
    ...plugin,
    capabilities: {
      ...plugin.capabilities,
      implementationProvider: true,
    },
    create(context) {
      const service = plugin.create(context)
      return {
        ...service,
        async provideImplementation(document, position, token) {
          const sourceUri = context.decodeEmbeddedDocumentUri?.(URI.parse(document.uri))?.[0].toString() ?? document.uri
          const extra = sourceUri !== document.uri
              || document.languageId === 'qin'
              || isQinDocumentUri(document.uri, sourceExtension)
            ? provideSourceImplementations(document, position, sourceUri)
            : []
          const original = await service.provideImplementation?.(document, position, token) ?? []
          return [
            ...extra,
            ...original,
          ]
        },
      }
    },
  }
}

function withCompletionLogging(plugin: LanguageServicePlugin): LanguageServicePlugin {
  return {
    ...plugin,
    create(context) {
      const service = plugin.create(context)
      if (!service.provideCompletionItems) {
        return service
      }
      return {
        ...service,
        async provideCompletionItems(document, position, completionContext, token) {
          logToFile('[Completion] request', JSON.stringify({
            plugin: plugin.name,
            uri: document.uri,
            languageId: document.languageId,
            position,
            triggerKind: completionContext?.triggerKind,
            triggerCharacter: completionContext?.triggerCharacter,
          }))
          const result = await service.provideCompletionItems!(document, position, completionContext, token)
          const labels = Array.isArray(result?.items)
            ? result.items.slice(0, 20).map(item => item.label)
            : []
          logToFile('[Completion] response', JSON.stringify({
            plugin: plugin.name,
            uri: document.uri,
            position,
            itemCount: result?.items?.length ?? 0,
            labels,
          }))
          return result
        },
      }
    },
  }
}
