/**
 * Java class proxy - wraps WASM exports to provide natural JS API
 */

import { loadWasmModule, getWasmExports } from "./loader";
import { toI32, fromI32, toWasmBool, fromWasmBool, WasmStringHelper } from "./types";
import type { WasmModuleMeta, MethodInfo } from "./bridge";

/**
 * Proxy for Java static methods
 */
export interface JavaClassProxy {
  [methodName: string]: (...args: any[]) => any;
  new(...args: any[]): JavaInstanceProxy;
}

/**
 * Proxy for Java instance methods and fields
 */
export interface JavaInstanceProxy {
  [key: string]: any;
}

/**
 * Create a proxy object for a Java class
 */
export async function createClassProxy(
  wasmPath: string,
  meta: WasmModuleMeta
): Promise<JavaClassProxy> {
  const instance = await loadWasmModule(wasmPath);
  const exports = getWasmExports(instance);
  const memory = exports.memory as WebAssembly.Memory | undefined;
  
  const stringHelper = memory ? new WasmStringHelper(memory) : null;

  // Create the proxy handler
  const handler: ProxyHandler<object> = {
    get(target, prop) {
      if (typeof prop !== "string") return undefined;

      // Check if it's a static method
      const staticMethod = meta.exports.staticMethods.find(m => m.name === prop);
      if (staticMethod) {
        return createMethodWrapper(exports, staticMethod, stringHelper);
      }

      // Check if it's in exports directly
      if (prop in exports) {
        const exp = exports[prop];
        if (typeof exp === "function") {
          return (...args: any[]) => exp(...args);
        }
        return exp;
      }

      return undefined;
    },

    construct(target, args) {
      // Handle `new ClassName(...)`
      const constructor = meta.exports.constructors[0];
      if (constructor && exports[constructor.name]) {
        const constructFn = exports[constructor.name] as Function;
        const ptr = constructFn(...args);
        return createInstanceProxy(exports, meta, ptr, stringHelper);
      }
      
      // Default: return empty instance proxy
      return createInstanceProxy(exports, meta, 0, stringHelper);
    },
  };

  return new Proxy(function() {}, handler) as unknown as JavaClassProxy;
}

/**
 * Create a wrapper function for a method
 */
function createMethodWrapper(
  exports: WebAssembly.Instance["exports"],
  method: MethodInfo,
  stringHelper: WasmStringHelper | null
): (...args: any[]) => any {
  const fn = exports[method.name];
  if (typeof fn !== "function") {
    return () => {
      throw new Error(`Method ${method.name} not found in WASM exports`);
    };
  }

  return (...args: any[]) => {
    // Convert arguments
    const convertedArgs = args.map((arg, i) => {
      const paramType = method.params[i]?.type || "int";
      return convertToWasm(arg, paramType);
    });

    // Call the function
    const result = (fn as Function)(...convertedArgs);

    // Convert result
    return convertFromWasm(result, method.returnType, stringHelper);
  };
}

/**
 * Create a proxy for a Java instance
 */
function createInstanceProxy(
  exports: WebAssembly.Instance["exports"],
  meta: WasmModuleMeta,
  instancePtr: number,
  stringHelper: WasmStringHelper | null
): JavaInstanceProxy {
  const handler: ProxyHandler<object> = {
    get(target, prop) {
      if (typeof prop !== "string") return undefined;

      // Check instance methods
      const method = meta.exports.instanceMethods.find(m => m.name === prop);
      if (method) {
        return (...args: any[]) => {
          const fn = exports[method.name];
          if (typeof fn !== "function") {
            throw new Error(`Method ${method.name} not found`);
          }
          // First arg is instance pointer
          const convertedArgs = [instancePtr, ...args.map((arg, i) => {
            const paramType = method.params[i]?.type || "int";
            return convertToWasm(arg, paramType);
          })];
          const result = (fn as Function)(...convertedArgs);
          return convertFromWasm(result, method.returnType, stringHelper);
        };
      }

      // Check fields
      const field = meta.exports.fields.find(f => f.name === prop && !f.isStatic);
      if (field) {
        const getter = exports[`get_${prop}`];
        if (typeof getter === "function") {
          return convertFromWasm((getter as Function)(instancePtr), field.type, stringHelper);
        }
      }

      return undefined;
    },

    set(target, prop, value) {
      if (typeof prop !== "string") return false;

      const field = meta.exports.fields.find(f => f.name === prop && !f.isStatic);
      if (field) {
        const setter = exports[`set_${prop}`];
        if (typeof setter === "function") {
          (setter as Function)(instancePtr, convertToWasm(value, field.type));
          return true;
        }
      }

      return false;
    },
  };

  return new Proxy({}, handler) as JavaInstanceProxy;
}

/**
 * Convert JS value to WASM value
 */
function convertToWasm(value: any, javaType: string): any {
  switch (javaType) {
    case "boolean":
      return toWasmBool(Boolean(value));
    case "byte":
    case "short":
    case "int":
      return toI32(Number(value));
    case "long":
      return BigInt(value);
    case "float":
    case "double":
      return Number(value);
    case "char":
      return typeof value === "string" ? value.charCodeAt(0) : Number(value);
    default:
      return value;
  }
}

/**
 * Convert WASM value to JS value
 */
function convertFromWasm(
  value: any,
  javaType: string,
  stringHelper: WasmStringHelper | null
): any {
  switch (javaType) {
    case "void":
      return undefined;
    case "boolean":
      return fromWasmBool(value);
    case "byte":
    case "short":
    case "int":
      return fromI32(value);
    case "long":
      return BigInt(value);
    case "float":
    case "double":
      return Number(value);
    case "char":
      return String.fromCharCode(value);
    case "String":
      if (stringHelper && typeof value === "number") {
        return stringHelper.readString(value);
      }
      return String(value);
    default:
      return value;
  }
}
