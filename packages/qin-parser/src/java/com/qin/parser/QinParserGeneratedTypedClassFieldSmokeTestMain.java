package com.qin.parser;

public final class QinParserGeneratedTypedClassFieldSmokeTestMain {
    private QinParserGeneratedTypedClassFieldSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                import { com_slime_ast_AstNode, com_slime_ast_AstNode as AstNode } from "../../AstNode.ts";
                import { com_slime_ast_AstNodeType, com_slime_ast_AstNodeType as AstNodeType } from "../../AstNodeType.ts";
                import { com_slime_ast_Misc, com_slime_ast_Misc as Misc } from "../../Misc.ts";
                import { com_slime_ast_SourceLocation, com_slime_ast_SourceLocation as SourceLocation } from "../../SourceLocation.ts";
                import { com_slime_ast_nodes_expressions_FunctionExpression, com_slime_ast_nodes_expressions_FunctionExpression as FunctionExpression } from "../expressions/FunctionExpression.ts";

                import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_logical__, __qin_java_string_hash_code__, __qin_java_identity_hash_code__, __qin_java_value_hash_code__, __qin_java_values_equal__, __qin_java_hash_key__, __qin_java_hash_key_equals__ } from "@qin/java-sdk-js";

                function __qin_structural_object__(value) {
                  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
                  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
                }
                class com_slime_ast_nodes_misc_MethodDefinition {
                  __qin_field_key: com_slime_ast_AstNode | null = null as any;
                  constructor(...__qin_args: any[]) {
                    switch (__qin_args.length) {
                      case 9: {
                        const key: any = __qin_args[0];
                        return;
                      }
                      default: throw new Error("Unsupported Java constructor arity: MethodDefinition/" + __qin_args.length);
                    }
                  }
                  type(): any {
                    return com_slime_ast_AstNodeType.__qin_field_METHOD_DEFINITION;
                  }
                }
                """);

        if (!parsed.hasProgram()) {
            throw new AssertionError("Expected generated TypeScript class field source to parse");
        }

        System.out.println("QinParserGeneratedTypedClassFieldSmokeTestMain OK");
    }
}
