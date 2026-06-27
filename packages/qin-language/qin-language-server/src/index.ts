import {
  createConnection,
  createServer,
  createTypeScriptProject,
  loadTsdkByPath,
} from '@volar/language-server/node'
import { create as createTypeScriptServices } from 'volar-service-typescript'
import { QinLanguagePlugin } from './QinLanguagePlugin'
import { QinLanguageServicePlugin } from './QinLanguageServicePlugin'
import { logToFile } from './logutil'

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

  const tsdk = loadTsdkByPath(tsdkPath, params.locale)
  const languagePlugins = [QinLanguagePlugin]
  const languageServicePlugins = [
    QinLanguageServicePlugin,
    ...createTypeScriptServices(tsdk.typescript, {
      isValidationEnabled(document) {
        return document.languageId !== 'qin' && !isQinDocumentUri(document.uri)
      },
    }),
  ]
  const tsProject = createTypeScriptProject(
    tsdk.typescript,
    tsdk.diagnosticMessages,
    () => ({
      languagePlugins,
    })
  )

  const result = server.initialize(params, tsProject, [...languageServicePlugins])
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

function isQinDocumentUri(uri: string): boolean {
  const lowerUri = uri.toLowerCase()
  return lowerUri.endsWith('.qin')
    || lowerUri.includes('.qin.')
    || lowerUri.includes('.qin%')
    || lowerUri.includes('%2eqin')
    || lowerUri.includes('%252eqin')
}
