/**
 * Java .class file parser
 * Parses bytecode to extract method and field information
 */

import { readFileSync } from "fs";

// Access flags
const ACC_PUBLIC = 0x0001;
const ACC_PRIVATE = 0x0002;
const ACC_PROTECTED = 0x0004;
const ACC_STATIC = 0x0008;
const ACC_FINAL = 0x0010;

/**
 * Parsed method information
 */
export interface ParsedMethod {
  name: string;
  descriptor: string;
  accessFlags: number;
  isPublic: boolean;
  isPrivate: boolean;
  isProtected: boolean;
  isStatic: boolean;
  params: string[];
  returnType: string;
}

/**
 * Parsed field information
 */
export interface ParsedField {
  name: string;
  descriptor: string;
  accessFlags: number;
  isPublic: boolean;
  isPrivate: boolean;
  isProtected: boolean;
  isStatic: boolean;
  type: string;
}

/**
 * Parsed class information
 */
export interface ParsedClass {
  className: string;
  superClassName: string;
  accessFlags: number;
  isPublic: boolean;
  methods: ParsedMethod[];
  fields: ParsedField[];
}

/**
 * Parse a Java type descriptor to human-readable type
 */
export function parseTypeDescriptor(descriptor: string): string {
  if (!descriptor) return "void";
  
  const char = descriptor[0];
  switch (char) {
    case "V": return "void";
    case "Z": return "boolean";
    case "B": return "byte";
    case "C": return "char";
    case "S": return "short";
    case "I": return "int";
    case "J": return "long";
    case "F": return "float";
    case "D": return "double";
    case "[": return parseTypeDescriptor(descriptor.slice(1)) + "[]";
    case "L": {
      const end = descriptor.indexOf(";");
      if (end === -1) return "Object";
      const className = descriptor.slice(1, end).replace(/\//g, ".");
      // Simplify common types
      if (className === "java.lang.String") return "String";
      if (className === "java.lang.Object") return "Object";
      return className;
    }
    default:
      return "unknown";
  }
}

/**
 * Parse method descriptor to get parameter types and return type
 */
export function parseMethodDescriptor(descriptor: string): { params: string[]; returnType: string } {
  const params: string[] = [];
  
  // Find the parameter section (between parentheses)
  const parenStart = descriptor.indexOf("(");
  const parenEnd = descriptor.indexOf(")");
  
  if (parenStart === -1 || parenEnd === -1) {
    return { params: [], returnType: "void" };
  }
  
  // Parse parameters
  let i = parenStart + 1;
  while (i < parenEnd) {
    const char = descriptor[i];
    
    if (char === "L") {
      // Object type
      const end = descriptor.indexOf(";", i);
      params.push(parseTypeDescriptor(descriptor.slice(i, end + 1)));
      i = end + 1;
    } else if (char === "[") {
      // Array type - find the element type
      let arrayDepth = 0;
      while (descriptor[i] === "[") {
        arrayDepth++;
        i++;
      }
      if (descriptor[i] === "L") {
        const end = descriptor.indexOf(";", i);
        const baseType = parseTypeDescriptor(descriptor.slice(i, end + 1));
        params.push(baseType + "[]".repeat(arrayDepth));
        i = end + 1;
      } else {
        const baseType = parseTypeDescriptor(descriptor[i]!);
        params.push(baseType + "[]".repeat(arrayDepth));
        i++;
      }
    } else {
      // Primitive type
      params.push(parseTypeDescriptor(char!));
      i++;
    }
  }
  
  // Parse return type
  const returnDescriptor = descriptor.slice(parenEnd + 1);
  const returnType = parseTypeDescriptor(returnDescriptor);
  
  return { params, returnType };
}

/**
 * Simple class file parser
 * Note: This is a simplified parser that extracts basic information
 */
export class ClassFileParser {
  private buffer: Buffer;
  private offset: number = 0;
  private constantPool: any[] = [];

  constructor(classFilePath: string) {
    this.buffer = readFileSync(classFilePath);
  }

  /**
   * Parse the class file and return class information
   */
  parse(): ParsedClass {
    // Check magic number
    const magic = this.readU4();
    if (magic !== 0xCAFEBABE) {
      throw new Error("Invalid class file: bad magic number");
    }

    // Skip version info
    this.readU2(); // minor version
    this.readU2(); // major version

    // Read constant pool
    const constantPoolCount = this.readU2();
    this.constantPool = [null]; // Index 0 is unused
    
    for (let i = 1; i < constantPoolCount; i++) {
      const entry = this.readConstantPoolEntry();
      this.constantPool.push(entry);
      // Long and Double take two entries
      if (entry.tag === 5 || entry.tag === 6) {
        this.constantPool.push(null);
        i++;
      }
    }

    // Read access flags
    const accessFlags = this.readU2();

    // Read this class
    const thisClassIndex = this.readU2();
    const className = this.getClassName(thisClassIndex);

    // Read super class
    const superClassIndex = this.readU2();
    const superClassName = superClassIndex > 0 ? this.getClassName(superClassIndex) : "java.lang.Object";

    // Skip interfaces
    const interfacesCount = this.readU2();
    for (let i = 0; i < interfacesCount; i++) {
      this.readU2();
    }

    // Read fields
    const fieldsCount = this.readU2();
    const fields: ParsedField[] = [];
    for (let i = 0; i < fieldsCount; i++) {
      fields.push(this.readField());
    }

    // Read methods
    const methodsCount = this.readU2();
    const methods: ParsedMethod[] = [];
    for (let i = 0; i < methodsCount; i++) {
      methods.push(this.readMethod());
    }

    return {
      className,
      superClassName,
      accessFlags,
      isPublic: (accessFlags & ACC_PUBLIC) !== 0,
      methods,
      fields,
    };
  }

  private readU1(): number {
    return this.buffer[this.offset++]!;
  }

  private readU2(): number {
    const value = this.buffer.readUInt16BE(this.offset);
    this.offset += 2;
    return value;
  }

  private readU4(): number {
    const value = this.buffer.readUInt32BE(this.offset);
    this.offset += 4;
    return value;
  }

  private readConstantPoolEntry(): any {
    const tag = this.readU1();
    
    switch (tag) {
      case 1: { // UTF8
        const length = this.readU2();
        const bytes = this.buffer.slice(this.offset, this.offset + length);
        this.offset += length;
        return { tag, value: bytes.toString("utf8") };
      }
      case 3: // Integer
        return { tag, value: this.readU4() };
      case 4: // Float
        return { tag, value: this.buffer.readFloatBE(this.offset - 4) };
      case 5: // Long
        const high = this.readU4();
        const low = this.readU4();
        return { tag, value: BigInt(high) << 32n | BigInt(low) };
      case 6: // Double
        return { tag, value: this.buffer.readDoubleBE(this.offset - 8) };
      case 7: // Class
        return { tag, nameIndex: this.readU2() };
      case 8: // String
        return { tag, stringIndex: this.readU2() };
      case 9: // Fieldref
      case 10: // Methodref
      case 11: // InterfaceMethodref
        return { tag, classIndex: this.readU2(), nameAndTypeIndex: this.readU2() };
      case 12: // NameAndType
        return { tag, nameIndex: this.readU2(), descriptorIndex: this.readU2() };
      case 15: // MethodHandle
        return { tag, referenceKind: this.readU1(), referenceIndex: this.readU2() };
      case 16: // MethodType
        return { tag, descriptorIndex: this.readU2() };
      case 18: // InvokeDynamic
        return { tag, bootstrapMethodAttrIndex: this.readU2(), nameAndTypeIndex: this.readU2() };
      default:
        throw new Error(`Unknown constant pool tag: ${tag}`);
    }
  }

  private getUtf8(index: number): string {
    const entry = this.constantPool[index];
    if (!entry || entry.tag !== 1) {
      return "";
    }
    return entry.value;
  }

  private getClassName(index: number): string {
    const entry = this.constantPool[index];
    if (!entry || entry.tag !== 7) {
      return "";
    }
    return this.getUtf8(entry.nameIndex).replace(/\//g, ".");
  }

  private readField(): ParsedField {
    const accessFlags = this.readU2();
    const nameIndex = this.readU2();
    const descriptorIndex = this.readU2();
    
    // Skip attributes
    const attributesCount = this.readU2();
    for (let i = 0; i < attributesCount; i++) {
      this.readU2(); // attribute name index
      const length = this.readU4();
      this.offset += length;
    }

    const name = this.getUtf8(nameIndex);
    const descriptor = this.getUtf8(descriptorIndex);

    return {
      name,
      descriptor,
      accessFlags,
      isPublic: (accessFlags & ACC_PUBLIC) !== 0,
      isPrivate: (accessFlags & ACC_PRIVATE) !== 0,
      isProtected: (accessFlags & ACC_PROTECTED) !== 0,
      isStatic: (accessFlags & ACC_STATIC) !== 0,
      type: parseTypeDescriptor(descriptor),
    };
  }

  private readMethod(): ParsedMethod {
    const accessFlags = this.readU2();
    const nameIndex = this.readU2();
    const descriptorIndex = this.readU2();
    
    // Skip attributes
    const attributesCount = this.readU2();
    for (let i = 0; i < attributesCount; i++) {
      this.readU2(); // attribute name index
      const length = this.readU4();
      this.offset += length;
    }

    const name = this.getUtf8(nameIndex);
    const descriptor = this.getUtf8(descriptorIndex);
    const { params, returnType } = parseMethodDescriptor(descriptor);

    return {
      name,
      descriptor,
      accessFlags,
      isPublic: (accessFlags & ACC_PUBLIC) !== 0,
      isPrivate: (accessFlags & ACC_PRIVATE) !== 0,
      isProtected: (accessFlags & ACC_PROTECTED) !== 0,
      isStatic: (accessFlags & ACC_STATIC) !== 0,
      params,
      returnType,
    };
  }
}

/**
 * Get public methods from a class file
 */
export function getPublicMethods(classFilePath: string): ParsedMethod[] {
  const parser = new ClassFileParser(classFilePath);
  const classInfo = parser.parse();
  return classInfo.methods.filter(m => m.isPublic);
}

/**
 * Get public fields from a class file
 */
export function getPublicFields(classFilePath: string): ParsedField[] {
  const parser = new ClassFileParser(classFilePath);
  const classInfo = parser.parse();
  return classInfo.fields.filter(f => f.isPublic);
}
