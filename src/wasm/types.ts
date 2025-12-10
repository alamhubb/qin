/**
 * Type conversion utilities between JavaScript and WASM
 */

/**
 * Convert JavaScript number to WASM i32
 */
export function toI32(value: number): number {
  return value | 0; // Truncate to 32-bit integer
}

/**
 * Convert WASM i32 to JavaScript number
 */
export function fromI32(value: number): number {
  return value | 0;
}

/**
 * Convert JavaScript number to WASM f64
 */
export function toF64(value: number): number {
  return +value; // Ensure it's a number
}

/**
 * Convert WASM f64 to JavaScript number
 */
export function fromF64(value: number): number {
  return +value;
}

/**
 * Convert JavaScript boolean to WASM i32 (0 or 1)
 */
export function toWasmBool(value: boolean): number {
  return value ? 1 : 0;
}

/**
 * Convert WASM i32 to JavaScript boolean
 */
export function fromWasmBool(value: number): boolean {
  return value !== 0;
}

/**
 * String encoding/decoding for WASM memory
 */
export class WasmStringHelper {
  private memory: WebAssembly.Memory;
  private encoder: TextEncoder;
  private decoder: TextDecoder;

  constructor(memory: WebAssembly.Memory) {
    this.memory = memory;
    this.encoder = new TextEncoder();
    this.decoder = new TextDecoder();
  }

  /**
   * Write a string to WASM memory at the given pointer
   * Returns the number of bytes written
   */
  writeString(ptr: number, str: string): number {
    const bytes = this.encoder.encode(str);
    const view = new Uint8Array(this.memory.buffer, ptr, bytes.length + 1);
    view.set(bytes);
    view[bytes.length] = 0; // Null terminator
    return bytes.length;
  }

  /**
   * Read a null-terminated string from WASM memory
   */
  readString(ptr: number): string {
    const view = new Uint8Array(this.memory.buffer, ptr);
    let end = 0;
    while (view[end] !== 0 && end < view.length) {
      end++;
    }
    return this.decoder.decode(view.subarray(0, end));
  }

  /**
   * Read a string with known length from WASM memory
   */
  readStringWithLength(ptr: number, length: number): string {
    const view = new Uint8Array(this.memory.buffer, ptr, length);
    return this.decoder.decode(view);
  }
}

/**
 * Type mapping from Java types to TypeScript types
 */
export const JAVA_TO_TS_TYPE_MAP: Record<string, string> = {
  "void": "void",
  "boolean": "boolean",
  "byte": "number",
  "short": "number",
  "int": "number",
  "long": "bigint",
  "float": "number",
  "double": "number",
  "char": "string",
  "String": "string",
  "Object": "any",
  "int[]": "Int32Array",
  "byte[]": "Uint8Array",
  "float[]": "Float32Array",
  "double[]": "Float64Array",
};

/**
 * Convert Java type to TypeScript type string
 */
export function javaTypeToTs(javaType: string): string {
  // Check direct mapping
  if (JAVA_TO_TS_TYPE_MAP[javaType]) {
    return JAVA_TO_TS_TYPE_MAP[javaType];
  }
  
  // Handle arrays
  if (javaType.endsWith("[]")) {
    const elementType = javaType.slice(0, -2);
    const tsElementType = javaTypeToTs(elementType);
    return `${tsElementType}[]`;
  }
  
  // Default to any for unknown types
  return "any";
}

/**
 * Type conversion result
 */
export interface TypeConversionResult<T> {
  success: boolean;
  value?: T;
  error?: string;
}

/**
 * Safe type conversion with error handling
 */
export function safeConvert<T>(
  value: unknown,
  converter: (v: unknown) => T
): TypeConversionResult<T> {
  try {
    return {
      success: true,
      value: converter(value),
    };
  } catch (e: any) {
    return {
      success: false,
      error: e.message,
    };
  }
}
