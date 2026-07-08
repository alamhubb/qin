function javaListToArray<T = any>(list: any): T[] {
  if (!list) return []
  if (Array.isArray(list)) return list as T[]
  if (Array.isArray(list.__items)) return list.__items as T[]
  if (typeof list.size === 'function' && typeof list.get === 'function') {
    const result: T[] = []
    for (let i = 0; i < list.size(); i++) {
      result.push(list.get(i))
    }
    return result
  }
  if (typeof list.size === 'number' && typeof list.get === 'function') {
    const result: T[] = []
    for (let i = 0; i < list.size; i++) {
      result.push(list.get(i))
    }
    return result
  }
  if (typeof list[Symbol.iterator] === 'function') {
    return Array.from(list) as T[]
  }
  return []
}

function readPosition(position: any): any {
  if (!position) return undefined
  const line = typeof position.getLine === 'function'
    ? position.getLine()
    : (typeof position.line === 'function' ? position.line() : position.line)
  const column = typeof position.getColumn === 'function'
    ? position.getColumn()
    : (typeof position.column === 'function' ? position.column() : position.column)
  const index = typeof position.getIndex === 'function'
    ? position.getIndex()
    : (typeof position.index === 'function' ? position.index() : position.index)
  if (line === undefined || line === null || column === undefined || column === null || index === undefined || index === null) {
    return undefined
  }
  return { line, column, index }
}

function normalizeGeneratedLocation(location: any, value?: string, type?: string): any {
  if (!location) return undefined
  const existingStart = typeof location.start === 'function' ? location.start() : location.start
  const existingEnd = typeof location.end === 'function' ? location.end() : location.end
  const start = readPosition(typeof location.getStart === 'function' ? location.getStart() : existingStart)
  const end = readPosition(typeof location.getEnd === 'function' ? location.getEnd() : existingEnd)
  if (!start || !end) return undefined
  const rawValue = typeof location.getValue === 'function'
    ? location.getValue()
    : (typeof location.value === 'function' ? location.value() : location.value)
  const rawType = typeof location.getType === 'function'
    ? location.getType()
    : (typeof location.type === 'function' ? location.type() : location.type)
  return {
    type: rawType ?? type,
    value: rawValue ?? value,
    newLine: typeof location.getNewLine === 'function'
      ? location.getNewLine()
      : (typeof location.newLine === 'function' ? location.newLine() : location.newLine),
    index: start.index,
    length: Math.max(0, end.index - start.index),
    start,
    end,
    filename: typeof location.getFilename === 'function'
      ? location.getFilename()
      : (typeof location.filename === 'function' ? location.filename() : location.filename),
    identifierName: typeof location.getIdentifierName === 'function'
      ? location.getIdentifierName()
      : (typeof location.identifierName === 'function' ? location.identifierName() : location.identifierName)
  }
}

export function normalizeGeneratedToken(token: any): any {
  if (!token || token.__csstsLegacyToken === true) return token
  const tokenName = typeof token.getTokenName === 'function' ? token.getTokenName() : (typeof token.tokenName === 'function' ? token.tokenName() : token.tokenName)
  const tokenValue = typeof token.getTokenValue === 'function' ? token.getTokenValue() : (typeof token.tokenValue === 'function' ? token.tokenValue() : token.tokenValue)
  const rowNum = typeof token.getRowNum === 'function' ? token.getRowNum() : (typeof token.rowNum === 'function' ? token.rowNum() : (token.rowNum ?? token.line))
  const columnStartNum = typeof token.getColumnStartNum === 'function' ? token.getColumnStartNum() : (typeof token.columnStartNum === 'function' ? token.columnStartNum() : (token.columnStartNum ?? token.column))
  const columnEndNum = typeof token.getColumnEndNum === 'function' ? token.getColumnEndNum() : (typeof token.columnEndNum === 'function' ? token.columnEndNum() : token.columnEndNum)
  const index = typeof token.getIndex === 'function' ? token.getIndex() : (typeof token.index === 'function' ? token.index() : (token.index ?? token.codeIndex))
  const hasLineBreakBefore = typeof token.hasLineBreakBefore === 'function'
    ? token.hasLineBreakBefore()
    : (typeof token.getHasLineBreakBefore === 'function' ? token.getHasLineBreakBefore() : token.hasLineBreakBefore)

  Object.defineProperties(token, {
    __csstsLegacyToken: { value: true, configurable: true },
    tokenName: { value: tokenName, configurable: true, writable: true },
    tokenValue: { value: tokenValue, configurable: true, writable: true },
    rowNum: { value: rowNum, configurable: true, writable: true },
    line: { value: rowNum, configurable: true, writable: true },
    columnStartNum: { value: columnStartNum, configurable: true, writable: true },
    column: { value: columnStartNum, configurable: true, writable: true },
    columnEndNum: { value: columnEndNum, configurable: true, writable: true },
    index: { value: index, configurable: true, writable: true },
    codeIndex: { value: index, configurable: true, writable: true },
    hasLineBreakBefore: { value: !!hasLineBreakBefore, configurable: true, writable: true }
  })
  return token
}

export function normalizeGeneratedTokens(tokens: any): any[] {
  return javaListToArray(tokens).map(token => normalizeGeneratedToken(token))
}

export function normalizeGeneratedCst<T = any>(cst: T): T {
  if (!cst || typeof cst !== 'object') return cst
  const node: any = cst
  if (node.__csstsLegacyCst === true) return cst
  const originalGetChildren = typeof node.getChildren === 'function' ? node.getChildren.bind(node) : undefined
  const originalGetChild = typeof node.getChild === 'function' ? node.getChild.bind(node) : undefined
  const originalGetToken = typeof node.getToken === 'function' ? node.getToken.bind(node) : undefined
  const originalGetLoc = typeof node.getLoc === 'function' ? node.getLoc.bind(node) : undefined
  const originalGetLocation = typeof node.getLocation === 'function' ? node.getLocation.bind(node) : undefined

  Object.defineProperties(node, {
    __csstsLegacyCst: { value: true, configurable: true },
    name: {
      configurable: true,
      enumerable: true,
      get() {
        return typeof node.getName === 'function' ? node.getName() : node.__qin_field_name
      }
    },
    value: {
      configurable: true,
      enumerable: true,
      get() {
        return typeof node.getValue === 'function' ? node.getValue() : node.__qin_field_value
      }
    },
    loc: {
      configurable: true,
      enumerable: true,
      get() {
        const raw = typeof node.getLoc === 'function'
          ? node.getLoc()
          : (typeof node.getLocation === 'function' ? node.getLocation() : node.__qin_field_loc)
        return normalizeGeneratedLocation(raw, node.value, node.name)
      }
    },
    children: {
      configurable: true,
      enumerable: true,
      get() {
        const raw = originalGetChildren ? originalGetChildren() : node.__qin_field_children
        return javaListToArray(raw).map(child => normalizeGeneratedCst(child))
      }
    }
  })
  const readNormalizedLoc = () => {
    const raw = originalGetLoc
      ? originalGetLoc()
      : (originalGetLocation ? originalGetLocation() : node.__qin_field_loc)
    return normalizeGeneratedLocation(raw, node.value, node.name)
  }
  if (!originalGetLoc) node.getLoc = readNormalizedLoc
  if (!originalGetLocation) node.getLocation = readNormalizedLoc
  if (!originalGetChildren) {
    node.getChildren = (name?: string) => {
      const children = node.children
      if (name === undefined) return children
      return children.filter((child: any) => child.name === name)
    }
  }
  if (!originalGetChild) {
    node.getChild = (name: string, index = 0) => node.children.filter((child: any) => child.name === name)[index]
  }
  if (!originalGetToken) {
    node.getToken = (tokenName: string) => node.children.find((child: any) => child.name === tokenName && child.value !== undefined && child.value !== null)
  }
  node.children.forEach((child: any) => normalizeGeneratedCst(child))
  return cst
}

function recordClassSimpleName(value: any): string | undefined {
  const recordName = value?.__qinJavaRecordClass
  if (typeof recordName !== 'string') return undefined
  const dotIndex = recordName.lastIndexOf('.')
  const simpleName = dotIndex >= 0 ? recordName.slice(dotIndex + 1) : recordName
  const nestedIndex = simpleName.lastIndexOf('$')
  return nestedIndex >= 0 ? simpleName.slice(nestedIndex + 1) : simpleName
}

function readGeneratedField(value: any, fieldName: string): any {
  if (!value || typeof value !== 'object') return undefined
  const direct = value[fieldName]
  if (direct !== undefined && typeof direct !== 'function') return direct
  const internal = value[`__${fieldName}`]
  if (internal !== undefined) return internal
  const qinField = value[`__qin_field_${fieldName}`]
  if (qinField !== undefined) return qinField
  if (typeof direct === 'function') return direct.call(value)
  const method = value[fieldName]
  if (typeof method === 'function') return method.call(value)
  const getterName = `get${fieldName.slice(0, 1).toUpperCase()}${fieldName.slice(1)}`
  const getter = value[getterName]
  return typeof getter === 'function' ? getter.call(value) : undefined
}

function pascalCaseEnumName(name: string): string {
  const parts = name.split('_').filter(Boolean)
  return parts.map(part => {
    if (part === 'TS') return 'TS'
    const lower = part.toLowerCase()
    return lower.slice(0, 1).toUpperCase() + lower.slice(1)
  }).join('')
}

function normalizeGeneratedAstType(value: any): string | undefined {
  if (typeof value === 'string') {
    return /^[A-Z][A-Z0-9_]*$/.test(value) ? pascalCaseEnumName(value) : value
  }
  const enumName = typeof value?.name === 'function' ? value.name() : value?.__qinEnumName
  if (typeof enumName === 'string' && enumName.length > 0) return pascalCaseEnumName(enumName)
  return undefined
}

function normalizeGeneratedAstList(value: any): any[] {
  return javaListToArray(value).map(item => normalizeGeneratedAst(item))
}

function normalizeGeneratedWrappedAstList(value: any, wrapperName: string): any[] {
  return javaListToArray(value).map(item => {
    const normalized = normalizeGeneratedAst(item)
    if (normalized == null || typeof normalized !== 'object') {
      return normalized
    }
    if (wrapperName in normalized) {
      normalized[wrapperName] = normalizeGeneratedAst(normalized[wrapperName])
      return normalized
    }
    return { [wrapperName]: normalized }
  })
}

function isGeneratedAstListField(nodeType: string, publicName: string): boolean {
  if (publicName === 'body') {
    return nodeType === 'Program'
      || nodeType === 'ClassBody'
      || nodeType === 'BlockStatement'
      || nodeType === 'StaticBlock'
      || nodeType === 'SwitchCase'
  }
  return publicName === 'params'
    || publicName === 'specifiers'
    || publicName === 'declarations'
    || publicName === 'arguments'
    || publicName === 'elements'
    || publicName === 'properties'
    || publicName === 'expressions'
    || publicName === 'decorators'
    || publicName === 'typeParameters'
    || publicName === 'implementsTypes'
}

const generatedAstArrayFields = [
  'body',
  'params',
  'specifiers',
  'declarations',
  'arguments',
  'elements',
  'properties',
  'expressions',
  'decorators',
  'typeParameters',
  'implementsTypes'
]

const generatedAstNodeFields = [
  'source',
  'imported',
  'local',
  'object',
  'property',
  'key',
  'value',
  'expression',
  'element',
  'argument',
  'callee',
  'left',
  'right',
  'test',
  'consequent',
  'alternate',
  'init',
  'id',
  'declaration',
  'discriminant',
  'body'
]

function normalizeGeneratedAstChildren(node: any) {
  const nodeType = normalizeGeneratedAstType(readGeneratedField(node, 'type')) ?? recordClassSimpleName(node)
  for (const field of generatedAstArrayFields) {
    const value = readGeneratedField(node, field)
    if (value !== undefined && nodeType && isGeneratedAstListField(nodeType, field)) {
      defineAstProperty(
        node,
        field,
        javaListToArray(value).map((item: any) => normalizeGeneratedAst(item)).filter((item: any) => field !== 'body' || (item && item.type))
      )
    }
  }
  for (const field of generatedAstNodeFields) {
    const value = readGeneratedField(node, field)
    if (value) defineAstProperty(node, field, normalizeGeneratedAst(value))
  }
}

function defineAstProperty(target: any, name: string, value: any) {
  if (value === undefined) return
  Object.defineProperty(target, name, {
    value,
    configurable: true,
    enumerable: true,
    writable: true
  })
}

function generatedFieldPublicName(key: string): string | undefined {
  if (key.startsWith('__qin_field___qin_')) return key.slice('__qin_field___qin_'.length)
  if (key.startsWith('__qin_field_')) return key.slice('__qin_field_'.length)
  if (key.startsWith('___qin_')) return key.slice('___qin_'.length)
  if (key.startsWith('__')) return key.slice(2)
  return undefined
}

export function normalizeGeneratedAst<T = any>(ast: T): T {
  if (!ast || typeof ast !== 'object') return ast
  const node: any = ast
  if (node.__csstsLegacyAst === true) {
    normalizeGeneratedAstChildren(node)
    return ast
  }

  const type = normalizeGeneratedAstType(readGeneratedField(node, 'type')) ?? recordClassSimpleName(node)
  if (!type) {
    if (Array.isArray(node)) {
      for (let i = 0; i < node.length; i++) node[i] = normalizeGeneratedAst(node[i])
      return ast
    }
    for (const key of Object.keys(node)) {
      const value = node[key]
      node[key] = Array.isArray(value)
        ? value.map(item => normalizeGeneratedAst(item))
        : normalizeGeneratedAst(value)
    }
    return ast
  }
  Object.defineProperty(node, '__csstsLegacyAst', { value: true, configurable: true })
  defineAstProperty(node, 'type', type)

  const value = readGeneratedField(node, 'value')
  defineAstProperty(node, 'value', value)

  const loc = normalizeGeneratedLocation(readGeneratedField(node, 'location'))
  defineAstProperty(node, 'loc', node.loc ?? loc)

  for (const key of Object.keys(node)) {
    if (key === '__qinJavaRecordClass' || key === '__csstsLegacyAst') continue
    const publicName = generatedFieldPublicName(key)
    if (!publicName) continue
    const value = (node as any)[key]
    if (publicName === 'location') continue
    if (type === 'ArrayExpression' && publicName === 'elements') {
      defineAstProperty(node, publicName, normalizeGeneratedWrappedAstList(value, 'element'))
    } else if (type === 'ObjectExpression' && publicName === 'properties') {
      defineAstProperty(node, publicName, normalizeGeneratedWrappedAstList(value, 'property'))
    } else if (isGeneratedAstListField(type, publicName)) {
      defineAstProperty(node, publicName, normalizeGeneratedAstList(value))
    } else {
      defineAstProperty(node, publicName, normalizeGeneratedAst(value))
    }
  }

  if (type === 'Property' || type === 'MethodDefinition') {
    const propertyValue = readGeneratedField(node, 'value')
    if (propertyValue !== undefined) defineAstProperty(node, 'value', normalizeGeneratedAst(propertyValue))
  }

  normalizeGeneratedAstChildren(node)

  return ast
}
