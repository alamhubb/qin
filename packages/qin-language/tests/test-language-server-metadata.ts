import {
  EXPECTED_QIN_LANGUAGE_SERVER_METADATA,
  resolveLanguageServerMetadata,
} from '../qin-language-server/src/LanguageServerMetadata'

const ENV_KEYS = [
  'QIN_LSP_SOURCE_EXTENSION',
  'QIN_LSP_SERVICE_EXTENSION',
  'QIN_LSP_GENERATED_PARSER_TARGET',
  'QIN_LSP_PARSER_PACKAGE',
  'QIN_LSP_COMPILER_PACKAGE',
] as const

function main() {
  withCleanEnv(() => {
    const metadata = resolveLanguageServerMetadata({
      qin: {
        languageServer: EXPECTED_QIN_LANGUAGE_SERVER_METADATA,
      },
    })
    assert(metadata.sourceExtension === '.qin', 'sourceExtension from initialization metadata')
    assert(metadata.parserPackage === 'com.qin:qin-parser', 'parserPackage from initialization metadata')
  })

  withCleanEnv(() => {
    process.env.QIN_LSP_SOURCE_EXTENSION = '.qin'
    process.env.QIN_LSP_SERVICE_EXTENSION = '.ts'
    process.env.QIN_LSP_GENERATED_PARSER_TARGET = '@qin/generated-qin-parser-ts'
    process.env.QIN_LSP_PARSER_PACKAGE = 'com.qin:qin-parser'
    const metadata = resolveLanguageServerMetadata({})
    assert(metadata.generatedParserTarget === '@qin/generated-qin-parser-ts', 'generated parser target from environment metadata')
  })

  withCleanEnv(() => {
    assertThrows(
      () => resolveLanguageServerMetadata({}),
      'requires qin.languageServer initialization metadata',
      'missing metadata must fail',
    )
  })

  withCleanEnv(() => {
    process.env.QIN_LSP_SOURCE_EXTENSION = '.ovs'
    process.env.QIN_LSP_SERVICE_EXTENSION = '.ts'
    process.env.QIN_LSP_GENERATED_PARSER_TARGET = '@qin/generated-qin-parser-ts'
    process.env.QIN_LSP_PARSER_PACKAGE = 'com.qin:qin-parser'
    assertThrows(
      () => resolveLanguageServerMetadata({
        qin: {
          languageServer: EXPECTED_QIN_LANGUAGE_SERVER_METADATA,
        },
      }),
      'initialization metadata.sourceExtension must match environment metadata.sourceExtension',
      'init/env mismatch must fail',
    )
  })

  console.log('Qin language server metadata smoke passed')
}

function withCleanEnv(run: () => void): void {
  const previous = new Map<string, string | undefined>()
  for (const key of ENV_KEYS) {
    previous.set(key, process.env[key])
    delete process.env[key]
  }
  try {
    run()
  } finally {
    for (const key of ENV_KEYS) {
      const value = previous.get(key)
      if (value === undefined) {
        delete process.env[key]
      } else {
        process.env[key] = value
      }
    }
  }
}

function assert(condition: boolean, message: string): void {
  if (!condition) {
    throw new Error(message)
  }
}

function assertThrows(run: () => void, expectedMessage: string, message: string): void {
  try {
    run()
  } catch (error) {
    const actualMessage = error instanceof Error ? error.message : String(error)
    if (!actualMessage.includes(expectedMessage)) {
      throw new Error(`${message}: expected "${expectedMessage}", got "${actualMessage}"`)
    }
    return
  }
  throw new Error(`${message}: expected error`)
}

main()
