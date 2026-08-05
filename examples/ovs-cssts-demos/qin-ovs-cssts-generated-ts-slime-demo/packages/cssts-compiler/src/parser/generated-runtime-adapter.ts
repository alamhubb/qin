import { __QinJavaUtilHashMap, __QinJavaUtilIdentityHashMap, __QinJavaUtilUnmodifiableMap } from "@qin/java-sdk-js"

type GeneratedJavaMapValue = __QinJavaUtilHashMap | __QinJavaUtilIdentityHashMap | __QinJavaUtilUnmodifiableMap

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
    : (isGeneratedMapLike(location)
      ? readGeneratedMapTypeValue(location)
      : (typeof location.type === 'function' ? location.type() : location.type))
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

function unwrapAccessorDescriptorValue(value: any, receiver?: any): any {
  let current = value
  for (let depth = 0; depth < 8; depth++) {
    if (current === null || current === undefined || typeof current !== 'object') return current
    if ((current as any).__qin_accessor_descriptor !== true) return current
    const storedValue = (current as any).value
    if (storedValue !== null && storedValue !== undefined && !isDescriptorText(storedValue)) {
      return storedValue
    }
    const getter = (current as any).get
    if (typeof getter !== 'function') return undefined
    current = receiver === undefined ? getter() : getter.call(receiver)
  }
  return current
}

function isDescriptorText(value: any): boolean {
  return typeof value === 'string' && value.startsWith('{__qin_accessor_descriptor=')
}

function readStaticMemberValue(member: any, receiver: any, fallback?: any): any {
  if (member !== undefined && typeof member !== 'function') return member
  if (typeof member === 'function') return member.call(receiver)
  return fallback
}

function isGeneratedMapLike(value: any): value is GeneratedJavaMapValue {
  return value instanceof __QinJavaUtilHashMap
    || value instanceof __QinJavaUtilIdentityHashMap
    || value instanceof __QinJavaUtilUnmodifiableMap
}

function readGeneratedMapTypeValue(value: GeneratedJavaMapValue | null | undefined): any {
  if (value === null || value === undefined) return undefined
  return value.get('type')
}

function descriptorFreeValue(value: any, receiver?: any): any {
  const unwrapped = unwrapAccessorDescriptorValue(value, receiver)
  return isDescriptorText(unwrapped) ? undefined : unwrapped
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
  const originalGetName = typeof node.getName === 'function' ? node.getName.bind(node) : undefined
  const originalGetValue = typeof node.getValue === 'function' ? node.getValue.bind(node) : undefined
  const originalGetChild = typeof node.getChild === 'function' ? node.getChild.bind(node) : undefined
  const originalGetToken = typeof node.getToken === 'function' ? node.getToken.bind(node) : undefined
  const originalGetLoc = typeof node.getLoc === 'function' ? node.getLoc.bind(node) : undefined
  const originalGetLocation = typeof node.getLocation === 'function' ? node.getLocation.bind(node) : undefined
  const readChildrenRaw = () => {
    if (Object.prototype.hasOwnProperty.call(node, '__qin_field_children')) {
      return node.__qin_field_children
    }
    return undefined
  }
  const normalizedName = descriptorFreeValue(node.__qin_field_name, node)
    ?? descriptorFreeValue(originalGetName ? originalGetName() : undefined, node)
  const normalizedValue = descriptorFreeValue(node.__qin_field_value, node)
    ?? descriptorFreeValue(originalGetValue ? originalGetValue() : undefined, node)
  const normalizedChildren = javaListToArray(descriptorFreeValue(readChildrenRaw(), node))
    .map(child => normalizeGeneratedCst(child))
  const normalizedLoc = normalizeGeneratedLocation(
    descriptorFreeValue(
      originalGetLoc
        ? originalGetLoc()
        : (originalGetLocation ? originalGetLocation() : node.__qin_field_loc),
      node
    ),
    normalizedValue,
    normalizedName
  )

  Object.defineProperties(node, {
    __csstsLegacyCst: { value: true, configurable: true },
    name: {
      value: normalizedName,
      configurable: true,
      enumerable: true,
      writable: true
    },
    value: {
      value: normalizedValue,
      configurable: true,
      enumerable: true,
      writable: true
    },
    loc: {
      value: normalizedLoc,
      configurable: true,
      enumerable: true,
      writable: true
    },
    children: {
      value: normalizedChildren,
      configurable: true,
      enumerable: true,
      writable: true
    }
  })
  Object.defineProperty(node, 'getName', {
    value: () => node.name,
    configurable: true,
    writable: true
  })
  Object.defineProperty(node, 'getValue', {
    value: () => node.value,
    configurable: true,
    writable: true
  })
  const readNormalizedLoc = () => {
    const raw = descriptorFreeValue(
      originalGetLoc
      ? originalGetLoc()
      : (originalGetLocation ? originalGetLocation() : node.__qin_field_loc),
      node
    )
    return normalizeGeneratedLocation(raw, node.value, node.name)
  }
  if (!originalGetLoc) node.getLoc = readNormalizedLoc
  if (!originalGetLocation) node.getLocation = readNormalizedLoc
  Object.defineProperty(node, 'getChildren', {
    value: (name?: string) => {
      const children = node.children
      if (name === undefined) return children
      return children.filter((child: any) => child.name === name)
    },
    configurable: true,
    writable: true
  })
  if (!originalGetChild) {
    node.getChild = (name: string, index = 0) => node.children.filter((child: any) => child.name === name)[index]
  }
  if (!originalGetToken) {
    node.getToken = (tokenName: string) => node.children.find((child: any) => child.name === tokenName && child.value !== undefined && child.value !== null)
  }
  return cst
}

function recordClassSimpleName(value: any): string | undefined {
  const recordName = value?.__qinJavaRecordClass
  if (typeof recordName !== 'string') return undefined
  return canonicalGeneratedTypeName(recordName)
}

type GeneratedFieldReader = {
  field: string
  read: (value: any) => any
}

const generatedFieldReaders: GeneratedFieldReader[] = [
  {
    field: 'type',
    read: (value: any) => isGeneratedMapLike(value)
      ? readGeneratedMapTypeValue(value)
      : readStaticMemberValue(value.type, value, value.__qin_field_type)
  },
  { field: 'value', read: (value: any) => readStaticMemberValue(value.value, value, value.__qin_field_value) },
  { field: 'location', read: (value: any) => readStaticMemberValue(value.location, value, value.__qin_field_location ?? value.__qin_field_loc) },
  { field: 'loc', read: (value: any) => readStaticMemberValue(value.loc, value, value.__qin_field_loc ?? value.__qin_field_location) },
  { field: 'kind', read: (value: any) => readStaticMemberValue(value.kind, value, value.__qin_field_kind) },
  { field: 'declarations', read: (value: any) => readStaticMemberValue(value.declarations, value, value.__qin_field_declarations) },
  { field: 'body', read: (value: any) => readStaticMemberValue(value.body, value, value.__qin_field_body) },
  { field: 'params', read: (value: any) => readStaticMemberValue(value.params, value, value.__qin_field_params) },
  { field: 'specifiers', read: (value: any) => readStaticMemberValue(value.specifiers, value, value.__qin_field_specifiers) },
  { field: 'arguments', read: (value: any) => readStaticMemberValue(value.arguments, value, value.__qin_field___qin_arguments ?? value.__qin_field_arguments) },
  { field: 'elements', read: (value: any) => readStaticMemberValue(value.elements, value, value.__qin_field_elements) },
  { field: 'properties', read: (value: any) => readStaticMemberValue(value.properties, value, value.__qin_field_properties) },
  { field: 'expressions', read: (value: any) => readStaticMemberValue(value.expressions, value, value.__qin_field_expressions) },
  { field: 'decorators', read: (value: any) => readStaticMemberValue(value.decorators, value, value.__qin_field_decorators) },
  { field: 'typeParameters', read: (value: any) => readStaticMemberValue(value.typeParameters, value, value.__qin_field_typeParameters) },
  { field: 'implementsTypes', read: (value: any) => readStaticMemberValue(value.implementsTypes, value, value.__qin_field_implementsTypes) },
  { field: 'source', read: (value: any) => readStaticMemberValue(value.source, value, value.__qin_field_source) },
  { field: 'imported', read: (value: any) => readStaticMemberValue(value.imported, value, value.__qin_field_imported) },
  { field: 'local', read: (value: any) => readStaticMemberValue(value.local, value, value.__qin_field_local) },
  { field: 'object', read: (value: any) => readStaticMemberValue(value.object, value, value.__qin_field_object) },
  { field: 'property', read: (value: any) => readStaticMemberValue(value.property, value, value.__qin_field_property) },
  { field: 'key', read: (value: any) => readStaticMemberValue(value.key, value, value.__qin_field_key) },
  { field: 'expression', read: (value: any) => readStaticMemberValue(value.expression, value, value.__qin_field_expression) },
  { field: 'element', read: (value: any) => readStaticMemberValue(value.element, value, value.__qin_field_element) },
  { field: 'argument', read: (value: any) => readStaticMemberValue(value.argument, value, value.__qin_field_argument) },
  { field: 'callee', read: (value: any) => readStaticMemberValue(value.callee, value, value.__qin_field_callee) },
  { field: 'left', read: (value: any) => readStaticMemberValue(value.left, value, value.__qin_field_left) },
  { field: 'right', read: (value: any) => readStaticMemberValue(value.right, value, value.__qin_field_right) },
  { field: 'test', read: (value: any) => readStaticMemberValue(value.test, value, value.__qin_field_test) },
  { field: 'consequent', read: (value: any) => readStaticMemberValue(value.consequent, value, value.__qin_field_consequent) },
  { field: 'alternate', read: (value: any) => readStaticMemberValue(value.alternate, value, value.__qin_field_alternate) },
  { field: 'init', read: (value: any) => readStaticMemberValue(value.init, value, value.__qin_field_init) },
  { field: 'update', read: (value: any) => readStaticMemberValue(value.update, value, value.__qin_field_update) },
  { field: 'id', read: (value: any) => readStaticMemberValue(value.id, value, value.__qin_field_id) },
  { field: 'declaration', read: (value: any) => readStaticMemberValue(value.declaration, value, value.__qin_field_declaration) },
  { field: 'discriminant', read: (value: any) => readStaticMemberValue(value.discriminant, value, value.__qin_field_discriminant) },
  { field: 'block', read: (value: any) => readStaticMemberValue(value.block, value, value.__qin_field_block) },
  { field: 'handler', read: (value: any) => readStaticMemberValue(value.handler, value, value.__qin_field_handler) },
  { field: 'finalizer', read: (value: any) => readStaticMemberValue(value.finalizer, value, value.__qin_field_finalizer) },
  { field: 'param', read: (value: any) => readStaticMemberValue(value.param, value, value.__qin_field_param) },
  { field: 'returnType', read: (value: any) => readStaticMemberValue(value.returnType, value, value.__qin_field_returnType) }
]

export function readGeneratedField(value: any, fieldName: string): any {
  if (!value || typeof value !== 'object') return undefined
  for (const reader of generatedFieldReaders) {
    if (reader.field === fieldName) return reader.read(value)
  }
  return undefined
}

export function readGeneratedEnumName(value: any): string | undefined {
  if (!value || typeof value !== 'object') return undefined
  const enumName = value.__qinEnumName
  return typeof enumName === 'string' && enumName.length > 0 ? enumName : undefined
}

function pascalCaseEnumName(name: string): string {
  const parts = name.split('_').filter(Boolean)
  return parts.map(part => {
    if (part === 'TS') return 'TS'
    const lower = part.toLowerCase()
    return lower.slice(0, 1).toUpperCase() + lower.slice(1)
  }).join('')
}

function canonicalGeneratedTypeName(name: string): string {
  const dotIndex = name.lastIndexOf('.')
  const withoutPackage = dotIndex >= 0 ? name.slice(dotIndex + 1) : name
  const dollarIndex = withoutPackage.lastIndexOf('$')
  const withoutNested = dollarIndex >= 0 ? withoutPackage.slice(dollarIndex + 1) : withoutPackage
  const underscoreIndex = withoutNested.lastIndexOf('_')
  return underscoreIndex >= 0 ? withoutNested.slice(underscoreIndex + 1) : withoutNested
}

function normalizeGeneratedAstType(value: any): string | undefined {
  if (typeof value === 'string') {
    if (/^[A-Z][A-Z0-9_]*$/.test(value)) return pascalCaseEnumName(value)
    const canonical = canonicalGeneratedTypeName(value)
    return canonical.length > 0 ? canonical : value
  }
  if (value === null || value === undefined) return undefined
  const generatedEnumName = readGeneratedEnumName(value)
  if (generatedEnumName !== undefined) {
    return /^[A-Z][A-Z0-9_]*$/.test(generatedEnumName) ? pascalCaseEnumName(generatedEnumName) : generatedEnumName
  }
  const enumName = String(value)
  if (enumName.length === 0 || enumName === '[object Object]') return undefined
  if (/^[A-Z][A-Z0-9_]*$/.test(enumName)) return pascalCaseEnumName(enumName)
  const canonical = canonicalGeneratedTypeName(enumName)
  return canonical.length > 0 ? canonical : enumName
}

export function normalizeGeneratedAstList(value: any): any[] {
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

type GeneratedAstChildFieldEntry = {
  nodeType: string
  fields: string[]
}

const generatedAstChildFieldTable: GeneratedAstChildFieldEntry[] = [
  { nodeType: 'Program', fields: ['body'] },
  { nodeType: 'ClassBody', fields: ['body'] },
  { nodeType: 'BlockStatement', fields: ['body'] },
  { nodeType: 'StaticBlock', fields: ['body'] },
  { nodeType: 'VariableDeclaration', fields: ['declarations'] },
  { nodeType: 'VariableDeclarator', fields: ['id', 'init'] },
  { nodeType: 'ImportDeclaration', fields: ['source', 'specifiers'] },
  { nodeType: 'ImportSpecifier', fields: ['imported', 'local'] },
  { nodeType: 'ImportDefaultSpecifier', fields: ['local'] },
  { nodeType: 'ImportNamespaceSpecifier', fields: ['local'] },
  { nodeType: 'ExportNamedDeclaration', fields: ['declaration', 'specifiers', 'source'] },
  { nodeType: 'ExportDefaultDeclaration', fields: ['declaration'] },
  { nodeType: 'ExportAllDeclaration', fields: ['source'] },
  { nodeType: 'ArrayExpression', fields: ['elements'] },
  { nodeType: 'ObjectExpression', fields: ['properties'] },
  { nodeType: 'Property', fields: ['key', 'value'] },
  { nodeType: 'MethodDefinition', fields: ['key', 'value'] },
  { nodeType: 'CallExpression', fields: ['callee', 'arguments'] },
  { nodeType: 'NewExpression', fields: ['callee', 'arguments'] },
  { nodeType: 'MemberExpression', fields: ['object', 'property'] },
  { nodeType: 'ExpressionStatement', fields: ['expression'] },
  { nodeType: 'ReturnStatement', fields: ['argument'] },
  { nodeType: 'ThrowStatement', fields: ['argument'] },
  { nodeType: 'AwaitExpression', fields: ['argument'] },
  { nodeType: 'SpreadElement', fields: ['argument'] },
  { nodeType: 'RestElement', fields: ['argument'] },
  { nodeType: 'ArrowFunctionExpression', fields: ['id', 'params', 'body', 'returnType', 'typeParameters'] },
  { nodeType: 'FunctionDeclaration', fields: ['id', 'params', 'body', 'returnType', 'typeParameters'] },
  { nodeType: 'FunctionExpression', fields: ['id', 'params', 'body', 'returnType', 'typeParameters'] },
  { nodeType: 'BinaryExpression', fields: ['left', 'right'] },
  { nodeType: 'LogicalExpression', fields: ['left', 'right'] },
  { nodeType: 'AssignmentExpression', fields: ['left', 'right'] },
  { nodeType: 'AssignmentPattern', fields: ['left', 'right'] },
  { nodeType: 'UnaryExpression', fields: ['argument'] },
  { nodeType: 'UpdateExpression', fields: ['argument'] },
  { nodeType: 'YieldExpression', fields: ['argument'] },
  { nodeType: 'ConditionalExpression', fields: ['test', 'consequent', 'alternate'] },
  { nodeType: 'IfStatement', fields: ['test', 'consequent', 'alternate'] },
  { nodeType: 'ForStatement', fields: ['init', 'test', 'update', 'body'] },
  { nodeType: 'ForInStatement', fields: ['left', 'right', 'body'] },
  { nodeType: 'ForOfStatement', fields: ['left', 'right', 'body'] },
  { nodeType: 'WhileStatement', fields: ['test', 'body'] },
  { nodeType: 'DoWhileStatement', fields: ['test', 'body'] },
  { nodeType: 'SwitchCase', fields: ['test', 'consequent'] },
  { nodeType: 'SwitchStatement', fields: ['discriminant'] },
  { nodeType: 'TryStatement', fields: ['block', 'handler', 'finalizer'] },
  { nodeType: 'CatchClause', fields: ['param', 'body'] },
  { nodeType: 'ArrayPattern', fields: ['elements'] },
  { nodeType: 'ObjectPattern', fields: ['properties'] },
  { nodeType: 'ParenthesizedExpression', fields: ['expression'] },
  { nodeType: 'SequenceExpression', fields: ['expressions'] },
  { nodeType: 'Decorator', fields: ['expression'] },
  { nodeType: 'ThisExpression', fields: [] },
  { nodeType: 'Super', fields: [] },
  { nodeType: 'Identifier', fields: [] },
  { nodeType: 'Literal', fields: [] },
  { nodeType: 'TemplateElement', fields: [] },
  { nodeType: 'EmptyStatement', fields: [] },
  { nodeType: 'DebuggerStatement', fields: [] },
  { nodeType: 'BreakStatement', fields: [] },
  { nodeType: 'ContinueStatement', fields: [] }
]

function generatedAstKnownChildFields(nodeType: string | undefined): string[] | undefined {
  if (!nodeType) return undefined
  for (const entry of generatedAstChildFieldTable) {
    if (entry.nodeType === nodeType) return entry.fields
  }
  return undefined
}

function normalizeGeneratedAstChildren(node: any) {
  if (node.__csstsLegacyAstChildrenNormalized === true) return
  const nodeType = normalizeGeneratedAstType(readGeneratedField(node, 'type')) ?? recordClassSimpleName(node)
  const knownFields = generatedAstKnownChildFields(nodeType)
  if (knownFields !== undefined) {
    for (const field of knownFields) {
      const value = readGeneratedField(node, field)
      if (value === undefined) continue
      if (nodeType && isGeneratedAstListField(nodeType, field)) {
        defineAstProperty(
          node,
          field,
          javaListToArray(value).map((item: any) => normalizeGeneratedAst(item)).filter((item: any) => field !== 'body' || hasGeneratedAstType(item))
        )
      } else if (value) {
        defineAstProperty(node, field, normalizeGeneratedAst(value))
      }
    }
  } else {
    for (const field of generatedAstArrayFields) {
      const value = readGeneratedField(node, field)
      if (value !== undefined && nodeType && isGeneratedAstListField(nodeType, field)) {
        defineAstProperty(
          node,
          field,
          javaListToArray(value).map((item: any) => normalizeGeneratedAst(item)).filter((item: any) => field !== 'body' || hasGeneratedAstType(item))
        )
      }
    }
    for (const field of generatedAstNodeFields) {
      const value = readGeneratedField(node, field)
      if (value) defineAstProperty(node, field, normalizeGeneratedAst(value))
    }
  }
  Object.defineProperty(node, '__csstsLegacyAstChildrenNormalized', { value: true, configurable: true })
}

function hasGeneratedAstType(item: any): boolean {
  if (!item) return false
  const type = normalizeGeneratedAstType(readGeneratedField(item, 'type')) ?? recordClassSimpleName(item)
  return typeof type === 'string' && type.length > 0
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
      const publicName = generatedFieldPublicName(key)
      if (!publicName) continue
      const value = readGeneratedField(node, publicName)
      defineAstProperty(node, key, Array.isArray(value)
        ? value.map(item => normalizeGeneratedAst(item))
        : normalizeGeneratedAst(value))
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
    const value = readGeneratedField(node, publicName)
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
