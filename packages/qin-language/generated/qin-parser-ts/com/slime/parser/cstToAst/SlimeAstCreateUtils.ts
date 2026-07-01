import { com_slime_ast_nodes_modules_ExportDefaultDeclaration, com_slime_ast_nodes_modules_ExportDefaultDeclaration as ExportDefaultDeclaration } from "../../ast/nodes/modules/ExportDefaultDeclaration.ts";
import { com_slime_ast_nodes_modules_ExportNamedDeclaration, com_slime_ast_nodes_modules_ExportNamedDeclaration as ExportNamedDeclaration } from "../../ast/nodes/modules/ExportNamedDeclaration.ts";
import { com_slime_ast_nodes_modules_ImportDeclaration, com_slime_ast_nodes_modules_ImportDeclaration as ImportDeclaration } from "../../ast/nodes/modules/ImportDeclaration.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../../subhuti/struct/SubhutiCst.ts";
import { com_subhuti_struct_SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation$Builder } from "../../../subhuti/struct/SubhutiSourceLocation.ts";
import { com_slime_ast_SourceLocation, com_slime_ast_SourceLocation as SourceLocation } from "../../ast/SourceLocation.ts";
import { com_slime_ast_Position, com_slime_ast_Position as Position } from "../../ast/Position.ts";
import { com_slime_ast_SyntaxToken, com_slime_ast_SyntaxToken as SyntaxToken } from "../../ast/SyntaxToken.ts";
import { com_slime_ast_Pattern, com_slime_ast_Pattern as Pattern } from "../../ast/Pattern.ts";
import { com_slime_ast_Expression, com_slime_ast_Expression as Expression } from "../../ast/Expression.ts";
import { com_slime_ast_nodes_expressions_ParenthesizedExpression, com_slime_ast_nodes_expressions_ParenthesizedExpression as ParenthesizedExpression } from "../../ast/nodes/expressions/ParenthesizedExpression.ts";
import { com_slime_ast_nodes_misc_SpreadElement, com_slime_ast_nodes_misc_SpreadElement as SpreadElement } from "../../ast/nodes/misc/SpreadElement.ts";
import { com_slime_ast_nodes_patterns_RestElement, com_slime_ast_nodes_patterns_RestElement as RestElement } from "../../ast/nodes/patterns/RestElement.ts";
import { com_slime_ast_nodes_expressions_ArrayExpression, com_slime_ast_nodes_expressions_ArrayExpression as ArrayExpression } from "../../ast/nodes/expressions/ArrayExpression.ts";
import { com_slime_ast_nodes_patterns_ArrayPattern, com_slime_ast_nodes_patterns_ArrayPattern as ArrayPattern } from "../../ast/nodes/patterns/ArrayPattern.ts";
import { com_slime_ast_nodes_expressions_ObjectExpression, com_slime_ast_nodes_expressions_ObjectExpression as ObjectExpression } from "../../ast/nodes/expressions/ObjectExpression.ts";
import { com_slime_ast_AstNode, com_slime_ast_AstNode as AstNode } from "../../ast/AstNode.ts";
import { com_slime_ast_nodes_misc_Property, com_slime_ast_nodes_misc_Property as Property } from "../../ast/nodes/misc/Property.ts";
import { com_slime_ast_nodes_patterns_ObjectPattern, com_slime_ast_nodes_patterns_ObjectPattern as ObjectPattern } from "../../ast/nodes/patterns/ObjectPattern.ts";
import { com_slime_ast_nodes_declarations_ClassDeclaration, com_slime_ast_nodes_declarations_ClassDeclaration as ClassDeclaration } from "../../ast/nodes/declarations/ClassDeclaration.ts";
import { com_slime_ast_nodes_expressions_Identifier, com_slime_ast_nodes_expressions_Identifier as Identifier } from "../../ast/nodes/expressions/Identifier.ts";
import { com_slime_ast_nodes_misc_ClassBody, com_slime_ast_nodes_misc_ClassBody as ClassBody } from "../../ast/nodes/misc/ClassBody.ts";
import { com_slime_ast_nodes_misc_Decorator, com_slime_ast_nodes_misc_Decorator as Decorator } from "../../ast/nodes/misc/Decorator.ts";
import { com_slime_ast_nodes_declarations_FunctionDeclaration, com_slime_ast_nodes_declarations_FunctionDeclaration as FunctionDeclaration } from "../../ast/nodes/declarations/FunctionDeclaration.ts";
import { com_slime_ast_nodes_statements_BlockStatement, com_slime_ast_nodes_statements_BlockStatement as BlockStatement } from "../../ast/nodes/statements/BlockStatement.ts";
import { com_slime_ast_nodes_declarations_VariableDeclaration, com_slime_ast_nodes_declarations_VariableDeclaration as VariableDeclaration } from "../../ast/nodes/declarations/VariableDeclaration.ts";
import { com_slime_ast_nodes_misc_VariableDeclarator, com_slime_ast_nodes_misc_VariableDeclarator as VariableDeclarator } from "../../ast/nodes/misc/VariableDeclarator.ts";
import { com_slime_ast_Statement, com_slime_ast_Statement as Statement } from "../../ast/Statement.ts";
import { com_slime_ast_nodes_statements_ExpressionStatement, com_slime_ast_nodes_statements_ExpressionStatement as ExpressionStatement } from "../../ast/nodes/statements/ExpressionStatement.ts";
import { com_slime_ast_nodes_statements_IfStatement, com_slime_ast_nodes_statements_IfStatement as IfStatement } from "../../ast/nodes/statements/IfStatement.ts";
import { com_slime_ast_nodes_statements_WhileStatement, com_slime_ast_nodes_statements_WhileStatement as WhileStatement } from "../../ast/nodes/statements/WhileStatement.ts";
import { com_slime_ast_nodes_statements_DoWhileStatement, com_slime_ast_nodes_statements_DoWhileStatement as DoWhileStatement } from "../../ast/nodes/statements/DoWhileStatement.ts";
import { com_slime_ast_nodes_statements_ForStatement, com_slime_ast_nodes_statements_ForStatement as ForStatement } from "../../ast/nodes/statements/ForStatement.ts";
import { com_slime_ast_nodes_statements_ForInStatement, com_slime_ast_nodes_statements_ForInStatement as ForInStatement } from "../../ast/nodes/statements/ForInStatement.ts";
import { com_slime_ast_nodes_statements_ForOfStatement, com_slime_ast_nodes_statements_ForOfStatement as ForOfStatement } from "../../ast/nodes/statements/ForOfStatement.ts";
import { com_slime_ast_nodes_statements_ReturnStatement, com_slime_ast_nodes_statements_ReturnStatement as ReturnStatement } from "../../ast/nodes/statements/ReturnStatement.ts";
import { com_slime_ast_nodes_statements_BreakStatement, com_slime_ast_nodes_statements_BreakStatement as BreakStatement } from "../../ast/nodes/statements/BreakStatement.ts";
import { com_slime_ast_nodes_statements_ContinueStatement, com_slime_ast_nodes_statements_ContinueStatement as ContinueStatement } from "../../ast/nodes/statements/ContinueStatement.ts";
import { com_slime_ast_nodes_statements_ThrowStatement, com_slime_ast_nodes_statements_ThrowStatement as ThrowStatement } from "../../ast/nodes/statements/ThrowStatement.ts";
import { com_slime_ast_nodes_statements_TryStatement, com_slime_ast_nodes_statements_TryStatement as TryStatement } from "../../ast/nodes/statements/TryStatement.ts";
import { com_slime_ast_nodes_misc_CatchClause, com_slime_ast_nodes_misc_CatchClause as CatchClause } from "../../ast/nodes/misc/CatchClause.ts";
import { com_slime_ast_nodes_statements_SwitchStatement, com_slime_ast_nodes_statements_SwitchStatement as SwitchStatement } from "../../ast/nodes/statements/SwitchStatement.ts";
import { com_slime_ast_nodes_misc_SwitchCase, com_slime_ast_nodes_misc_SwitchCase as SwitchCase } from "../../ast/nodes/misc/SwitchCase.ts";
import { com_slime_ast_nodes_statements_LabeledStatement, com_slime_ast_nodes_statements_LabeledStatement as LabeledStatement } from "../../ast/nodes/statements/LabeledStatement.ts";
import { com_slime_ast_nodes_statements_WithStatement, com_slime_ast_nodes_statements_WithStatement as WithStatement } from "../../ast/nodes/statements/WithStatement.ts";
import { com_slime_ast_nodes_statements_DebuggerStatement, com_slime_ast_nodes_statements_DebuggerStatement as DebuggerStatement } from "../../ast/nodes/statements/DebuggerStatement.ts";
import { com_slime_ast_nodes_statements_EmptyStatement, com_slime_ast_nodes_statements_EmptyStatement as EmptyStatement } from "../../ast/nodes/statements/EmptyStatement.ts";
import { com_slime_ast_nodes_expressions_Literal, com_slime_ast_nodes_expressions_Literal as Literal, com_slime_ast_nodes_expressions_Literal$BigintValue, com_slime_ast_nodes_expressions_Literal$BigintValue as BigintValue } from "../../ast/nodes/expressions/Literal.ts";
import { com_slime_ast_nodes_expressions_TemplateLiteral, com_slime_ast_nodes_expressions_TemplateLiteral as TemplateLiteral } from "../../ast/nodes/expressions/TemplateLiteral.ts";
import { com_slime_ast_nodes_misc_TemplateElement, com_slime_ast_nodes_misc_TemplateElement as TemplateElement } from "../../ast/nodes/misc/TemplateElement.ts";
import { com_slime_ast_nodes_expressions_TaggedTemplateExpression, com_slime_ast_nodes_expressions_TaggedTemplateExpression as TaggedTemplateExpression } from "../../ast/nodes/expressions/TaggedTemplateExpression.ts";
import { com_slime_ast_Declaration, com_slime_ast_Declaration as Declaration } from "../../ast/Declaration.ts";
import { com_slime_ast_nodes_misc_ExportSpecifier, com_slime_ast_nodes_misc_ExportSpecifier as ExportSpecifier } from "../../ast/nodes/misc/ExportSpecifier.ts";
import { com_slime_ast_nodes_misc_Program, com_slime_ast_nodes_misc_Program as Program } from "../../ast/nodes/misc/Program.ts";
import { com_slime_ast_nodes_expressions_BinaryExpression, com_slime_ast_nodes_expressions_BinaryExpression as BinaryExpression } from "../../ast/nodes/expressions/BinaryExpression.ts";
import { com_slime_ast_nodes_expressions_LogicalExpression, com_slime_ast_nodes_expressions_LogicalExpression as LogicalExpression } from "../../ast/nodes/expressions/LogicalExpression.ts";
import { com_slime_ast_nodes_expressions_AssignmentExpression, com_slime_ast_nodes_expressions_AssignmentExpression as AssignmentExpression } from "../../ast/nodes/expressions/AssignmentExpression.ts";
import { com_slime_ast_nodes_expressions_UnaryExpression, com_slime_ast_nodes_expressions_UnaryExpression as UnaryExpression } from "../../ast/nodes/expressions/UnaryExpression.ts";
import { com_slime_ast_nodes_expressions_UpdateExpression, com_slime_ast_nodes_expressions_UpdateExpression as UpdateExpression } from "../../ast/nodes/expressions/UpdateExpression.ts";
import { com_slime_ast_nodes_expressions_SequenceExpression, com_slime_ast_nodes_expressions_SequenceExpression as SequenceExpression } from "../../ast/nodes/expressions/SequenceExpression.ts";
import { com_slime_ast_nodes_expressions_ConditionalExpression, com_slime_ast_nodes_expressions_ConditionalExpression as ConditionalExpression } from "../../ast/nodes/expressions/ConditionalExpression.ts";
import { com_slime_ast_nodes_expressions_CallExpression, com_slime_ast_nodes_expressions_CallExpression as CallExpression } from "../../ast/nodes/expressions/CallExpression.ts";
import { com_slime_ast_nodes_expressions_NewExpression, com_slime_ast_nodes_expressions_NewExpression as NewExpression } from "../../ast/nodes/expressions/NewExpression.ts";
import { com_slime_ast_nodes_expressions_MemberExpression, com_slime_ast_nodes_expressions_MemberExpression as MemberExpression } from "../../ast/nodes/expressions/MemberExpression.ts";
import { com_slime_ast_nodes_expressions_YieldExpression, com_slime_ast_nodes_expressions_YieldExpression as YieldExpression } from "../../ast/nodes/expressions/YieldExpression.ts";
import { com_slime_ast_nodes_expressions_AwaitExpression, com_slime_ast_nodes_expressions_AwaitExpression as AwaitExpression } from "../../ast/nodes/expressions/AwaitExpression.ts";
import { com_slime_ast_nodes_expressions_ThisExpression, com_slime_ast_nodes_expressions_ThisExpression as ThisExpression } from "../../ast/nodes/expressions/ThisExpression.ts";
import { com_slime_ast_nodes_patterns_AssignmentPattern, com_slime_ast_nodes_patterns_AssignmentPattern as AssignmentPattern } from "../../ast/nodes/patterns/AssignmentPattern.ts";
import { com_slime_ast_nodes_expressions_FunctionExpression, com_slime_ast_nodes_expressions_FunctionExpression as FunctionExpression } from "../../ast/nodes/expressions/FunctionExpression.ts";
import { com_slime_ast_nodes_misc_FunctionParameter, com_slime_ast_nodes_misc_FunctionParameter as FunctionParameter } from "../../ast/nodes/misc/FunctionParameter.ts";
import { com_slime_ast_nodes_expressions_ClassExpression, com_slime_ast_nodes_expressions_ClassExpression as ClassExpression } from "../../ast/nodes/expressions/ClassExpression.ts";
import { com_slime_ast_nodes_misc_MethodDefinition, com_slime_ast_nodes_misc_MethodDefinition as MethodDefinition } from "../../ast/nodes/misc/MethodDefinition.ts";
import { com_slime_ast_nodes_misc_PropertyDefinition, com_slime_ast_nodes_misc_PropertyDefinition as PropertyDefinition } from "../../ast/nodes/misc/PropertyDefinition.ts";
import { com_slime_ast_nodes_expressions_ArrowFunctionExpression, com_slime_ast_nodes_expressions_ArrowFunctionExpression as ArrowFunctionExpression } from "../../ast/nodes/expressions/ArrowFunctionExpression.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_logical__, __qin_java_implements, __QinJavaUtilObjects, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __QinJavaUtilList, __QinJavaUtilArrays, __QinJavaUtilRegexPattern } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const Objects = __QinJavaUtilObjects;
const ArrayList = __QinJavaUtilArrayList;
const Arrays = __QinJavaUtilArrays;
class com_slime_parser_cstToAst_SlimeAstCreateUtils {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SlimeAstCreateUtils/" + __qin_args.length);
    }
  }
  static toSourceLocation(loc: com_subhuti_struct_SubhutiSourceLocation): any {
    if (__qin_binary__("==", loc, null)) {
      return null;
    }
    let start: any = loc.start();
    let end: any = loc.end();
    if ((() => {
      if (__qin_binary__("==", start, null)) {
        return true;
      }
      return __qin_binary__("==", end, null);
    })()) {
      return null;
    }
    return new com_slime_ast_SourceLocation(loc.getType(), loc.getValue(), new com_slime_ast_Position(start.line(), start.column(), start.index()), new com_slime_ast_Position(end.line(), end.column(), end.index()));
  }
  static resolveSourceLocation(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSubhutiLocation(cst));
  }
  static createSyntaxToken(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createSyntaxToken_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createSyntaxToken_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiCst)) return this.__qin_overload_createSyntaxToken_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createSyntaxToken/" + __qin_args.length);
  }
  static __qin_overload_createSyntaxToken_3_0(type: string, value: string, location: com_slime_ast_SourceLocation): any {
    if (__qin_binary__("==", location, null)) {
      return null;
    }
    return new com_slime_ast_SyntaxToken(type, value, location);
  }
  static __qin_overload_createSyntaxToken_3_1(type: string, value: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createSyntaxToken(type, value, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createSyntaxToken_3_2(type: string, value: string, cst: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", cst, null)) {
      return null;
    }
    let location: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSourceLocation(cst);
    if (__qin_binary__("==", location, null)) {
      return null;
    }
    if ((() => {
      if (__qin_binary__("==", location.type(), null)) {
        return true;
      }
      return __qin_binary__("==", location.value(), null);
    })()) {
      location = new com_slime_ast_SourceLocation((() => {
      if (__qin_binary__("==", location.type(), null)) {
        return cst.getName();
      }
      return location.type();
    })(), (() => {
      if (__qin_binary__("==", location.value(), null)) {
        return cst.getValue();
      }
      return location.value();
    })(), location.start(), location.end());
    }
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createSyntaxToken(type, value, location);
  }
  static createSyntheticSourceLocation(type: string): any {
    return new com_slime_ast_SourceLocation(type, null, new com_slime_ast_Position(0.0, 0.0, 0.0), new com_slime_ast_Position(0.0, 0.0, 0.0));
  }
  static enrichSourceLocation(location: com_slime_ast_SourceLocation, defaultType: string, defaultValue: string): any {
    if (__qin_binary__("==", location, null)) {
      return null;
    }
    let resolvedType: any = (() => {
      if (__qin_binary__("!=", location.type(), null)) {
        return location.type();
      }
      return defaultType;
    })();
    let resolvedValue: any = (() => {
      if (__qin_binary__("!=", location.value(), null)) {
        return location.value();
      }
      return defaultValue;
    })();
    if ((() => {
      if (__QinJavaUtilObjects.equals(resolvedType, location.type())) {
        return __QinJavaUtilObjects.equals(resolvedValue, location.value());
      }
      return false;
    })()) {
      return location;
    }
    return new com_slime_ast_SourceLocation(resolvedType, resolvedValue, location.start(), location.end());
  }
  static resolveSubhutiLocation(cst: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", cst, null)) {
      return null;
    }
    let direct: any = cst.getLocation();
    if ((() => {
      if ((() => {
      if (__qin_binary__("!=", direct, null)) {
        return __qin_binary__("!=", direct.start(), null);
      }
      return false;
    })()) {
        return __qin_binary__("!=", direct.end(), null);
      }
      return false;
    })()) {
      let resolvedType: any = (() => {
      if (__qin_binary__("!=", direct.getType(), null)) {
        return direct.getType();
      }
      return cst.getName();
    })();
      let resolvedValue: any = (() => {
      if (__qin_binary__("!=", direct.getValue(), null)) {
        return direct.getValue();
      }
      return cst.getValue();
    })();
      if ((() => {
      if (__QinJavaUtilObjects.equals(resolvedType, direct.getType())) {
        return __QinJavaUtilObjects.equals(resolvedValue, direct.getValue());
      }
      return false;
    })()) {
        return direct;
      }
      return new com_subhuti_struct_SubhutiSourceLocation(resolvedType, resolvedValue, direct.getNewLine(), direct.start(), direct.end(), direct.getFilename(), direct.getIdentifierName());
    }
    let first: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.findFirstDescendantLocation(cst);
    let last: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.findLastDescendantLocation(cst);
    if ((() => {
      if ((() => {
      if ((() => {
      if (__qin_binary__("==", first, null)) {
        return true;
      }
      return __qin_binary__("==", last, null);
    })()) {
        return true;
      }
      return __qin_binary__("==", first.start(), null);
    })()) {
        return true;
      }
      return __qin_binary__("==", last.end(), null);
    })()) {
      return direct;
    }
    return new com_subhuti_struct_SubhutiSourceLocation(cst.getName(), cst.getValue(), (() => {
      if (__qin_binary__("==", direct, null)) {
        return null;
      }
      return direct.getNewLine();
    })(), first.start(), last.end(), (() => {
      if (__qin_binary__("==", direct, null)) {
        return null;
      }
      return direct.getFilename();
    })(), (() => {
      if (__qin_binary__("==", direct, null)) {
        return null;
      }
      return direct.getIdentifierName();
    })());
  }
  static findFirstDescendantLocation(cst: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", cst, null)) {
      return null;
    }
    let direct: any = cst.getLocation();
    if ((() => {
      if ((() => {
      if (__qin_binary__("!=", direct, null)) {
        return __qin_binary__("!=", direct.start(), null);
      }
      return false;
    })()) {
        return __qin_binary__("!=", direct.end(), null);
      }
      return false;
    })()) {
      return direct;
    }
    if (__qin_binary__("==", cst.getChildren(), null)) {
      return null;
    }
    for (const child of cst.getChildren()) {
      let found: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.findFirstDescendantLocation(child);
      if (__qin_binary__("!=", found, null)) {
        return found;
      }
    }
    return null;
  }
  static findLastDescendantLocation(cst: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", cst, null)) {
      return null;
    }
    if (__qin_binary__("!=", cst.getChildren(), null)) {
      for (let i: any = __qin_binary__("-", cst.getChildren().size(), 1.0); __qin_binary__(">=", i, 0.0); i = __qin_binary__("-", i, 1.0)) {
        let found: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.findLastDescendantLocation(cst.getChildren().get(i));
        if (__qin_binary__("!=", found, null)) {
          return found;
        }
      }
    }
    let direct: any = cst.getLocation();
    if ((() => {
      if ((() => {
      if (__qin_binary__("!=", direct, null)) {
        return __qin_binary__("!=", direct.start(), null);
      }
      return false;
    })()) {
        return __qin_binary__("!=", direct.end(), null);
      }
      return false;
    })()) {
      return direct;
    }
    return null;
  }
  static filterList(values: any[], type: any): any {
    let result: any = new __QinJavaUtilArrayList();
    if (__qin_binary__("==", values, null)) {
      return result;
    }
    for (const value of values) {
      if (type.isInstance(value)) {
        result.add(type.cast(value));
      }
    }
    return result;
  }
  static toPattern(expr: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    let pattern: any = null;
    let parenthesizedExpression: any = null;
    let spreadElement: any = null;
    let arrayExpression: any = null;
    let objectExpression: any = null;
    let objectProperty: any = null;
    if ((() => { const __qin_pattern_value = expr; return __qin_java_implements(__qin_pattern_value, "com.slime.ast.Pattern") && (pattern = __qin_pattern_value, true); })()) {
      return pattern;
    }
    if ((() => { const __qin_pattern_value = expr; return __qin_pattern_value instanceof com_slime_ast_nodes_expressions_ParenthesizedExpression && (parenthesizedExpression = __qin_pattern_value, true); })()) {
      return com_slime_parser_cstToAst_SlimeAstCreateUtils.toPattern(parenthesizedExpression.expression(), parenthesizedExpression.location());
    }
    if ((() => { const __qin_pattern_value = expr; return __qin_pattern_value instanceof com_slime_ast_nodes_misc_SpreadElement && (spreadElement = __qin_pattern_value, true); })()) {
      return new com_slime_ast_nodes_patterns_RestElement(com_slime_parser_cstToAst_SlimeAstCreateUtils.toPattern(spreadElement.argument(), spreadElement.argument().location()), spreadElement.location());
    }
    if ((() => { const __qin_pattern_value = expr; return __qin_pattern_value instanceof com_slime_ast_nodes_expressions_ArrayExpression && (arrayExpression = __qin_pattern_value, true); })()) {
      let elements: any = new __QinJavaUtilArrayList();
      for (const element of arrayExpression.elements()) {
        elements.add((() => {
      if (__qin_binary__("==", element, null)) {
        return null;
      }
      return com_slime_parser_cstToAst_SlimeAstCreateUtils.toPattern(element, element.location());
    })());
      }
      return new com_slime_ast_nodes_patterns_ArrayPattern(elements, arrayExpression.location());
    }
    if ((() => { const __qin_pattern_value = expr; return __qin_pattern_value instanceof com_slime_ast_nodes_expressions_ObjectExpression && (objectExpression = __qin_pattern_value, true); })()) {
      let properties: any = new __QinJavaUtilArrayList();
      for (const property of objectExpression.properties()) {
        if ((() => { const __qin_pattern_value = property; return __qin_pattern_value instanceof com_slime_ast_nodes_misc_Property && (objectProperty = __qin_pattern_value, true); })()) {
          properties.add(objectProperty);
        } else {
          if ((() => { const __qin_pattern_value = property; return __qin_pattern_value instanceof com_slime_ast_nodes_misc_SpreadElement && (spreadElement = __qin_pattern_value, true); })()) {
            properties.add(new com_slime_ast_nodes_patterns_RestElement(com_slime_parser_cstToAst_SlimeAstCreateUtils.toPattern(spreadElement.argument(), spreadElement.argument().location()), spreadElement.location()));
          }
        }
      }
      return new com_slime_ast_nodes_patterns_ObjectPattern(properties, objectExpression.location());
    }
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createIdentifier("_", location);
  }
  static createClassDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && true && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SourceLocation || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createClassDeclaration_5_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.slime.ast.AstNode")) && (__qin_args[4] === null || Array.isArray(__qin_args[4]) || __qin_args[4] instanceof __QinJavaUtilArrayList || __qin_args[4] instanceof __QinJavaUtilUnmodifiableList) && true && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SourceLocation || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createClassDeclaration_7_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], (Array.isArray(__qin_args[4]) ? new __QinJavaUtilArrayList(__qin_args[4]) : __qin_args[4]), __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || Array.isArray(__qin_args[3]) || __qin_args[3] instanceof __QinJavaUtilArrayList || __qin_args[3] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[4] === null || __qin_java_implements(__qin_args[4], "com.slime.ast.AstNode")) && (__qin_args[5] === null || Array.isArray(__qin_args[5]) || __qin_args[5] instanceof __QinJavaUtilArrayList || __qin_args[5] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SourceLocation || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createClassDeclaration_7_2(__qin_args[0], __qin_args[1], __qin_args[2], (Array.isArray(__qin_args[3]) ? new __QinJavaUtilArrayList(__qin_args[3]) : __qin_args[3]), __qin_args[4], (Array.isArray(__qin_args[5]) ? new __QinJavaUtilArrayList(__qin_args[5]) : __qin_args[5]), __qin_args[6]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && true && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassDeclaration_5_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.slime.ast.AstNode")) && (__qin_args[4] === null || Array.isArray(__qin_args[4]) || __qin_args[4] instanceof __QinJavaUtilArrayList || __qin_args[4] instanceof __QinJavaUtilUnmodifiableList) && true && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassDeclaration_7_4(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], (Array.isArray(__qin_args[4]) ? new __QinJavaUtilArrayList(__qin_args[4]) : __qin_args[4]), __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || Array.isArray(__qin_args[3]) || __qin_args[3] instanceof __QinJavaUtilArrayList || __qin_args[3] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[4] === null || __qin_java_implements(__qin_args[4], "com.slime.ast.AstNode")) && (__qin_args[5] === null || Array.isArray(__qin_args[5]) || __qin_args[5] instanceof __QinJavaUtilArrayList || __qin_args[5] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassDeclaration_7_5(__qin_args[0], __qin_args[1], __qin_args[2], (Array.isArray(__qin_args[3]) ? new __QinJavaUtilArrayList(__qin_args[3]) : __qin_args[3]), __qin_args[4], (Array.isArray(__qin_args[5]) ? new __QinJavaUtilArrayList(__qin_args[5]) : __qin_args[5]), __qin_args[6]);
    throw new Error("Unsupported Java overload: createClassDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createClassDeclaration_5_0(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any[], location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassDeclaration(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), null, __QinJavaUtilList.of(), location);
  }
  static __qin_overload_createClassDeclaration_7_1(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, typeParameters: com_slime_ast_AstNode, implementsTypes: any, decorators: any[], location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassDeclaration(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), typeParameters, implementsTypes, location);
  }
  static __qin_overload_createClassDeclaration_7_2(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any, typeParameters: com_slime_ast_AstNode, implementsTypes: any, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_declarations_ClassDeclaration(id, superClass, body, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), typeParameters, (() => {
      if (__qin_binary__("==", implementsTypes, null)) {
        return __QinJavaUtilList.of();
      }
      return implementsTypes;
    })(), location);
  }
  static __qin_overload_createClassDeclaration_5_3(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassDeclaration(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), null, __QinJavaUtilList.of(), location);
  }
  static __qin_overload_createClassDeclaration_7_4(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, typeParameters: com_slime_ast_AstNode, implementsTypes: any, decorators: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassDeclaration(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), typeParameters, implementsTypes, location);
  }
  static __qin_overload_createClassDeclaration_7_5(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any, typeParameters: com_slime_ast_AstNode, implementsTypes: any, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_declarations_ClassDeclaration(id, superClass, body, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), typeParameters, (() => {
      if (__qin_binary__("==", implementsTypes, null)) {
        return __QinJavaUtilList.of();
      }
      return implementsTypes;
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createFunctionDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 6 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && true && (__qin_args[2] === null || __qin_args[2] instanceof Object) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SourceLocation || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createFunctionDeclaration_6_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_args[1] instanceof __QinJavaUtilArrayList || __qin_args[1] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_args[2] instanceof __QinJavaUtilArrayList || __qin_args[2] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[3].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_java_implements(__qin_args[6], "com.slime.ast.AstNode")) && (__qin_args[7] === null || __qin_java_implements(__qin_args[7], "com.slime.ast.AstNode")) && (__qin_args[8] === null || __qin_args[8] instanceof com_slime_ast_SourceLocation || __qin_args[8].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createFunctionDeclaration_9_1(__qin_args[0], (Array.isArray(__qin_args[1]) ? new __QinJavaUtilArrayList(__qin_args[1]) : __qin_args[1]), (Array.isArray(__qin_args[2]) ? new __QinJavaUtilArrayList(__qin_args[2]) : __qin_args[2]), __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    if (__qin_args.length === 6 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || __qin_args[5] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createFunctionDeclaration_6_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && true && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_args[2] instanceof __QinJavaUtilArrayList || __qin_args[2] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[3].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_java_implements(__qin_args[6], "com.slime.ast.AstNode")) && (__qin_args[7] === null || __qin_java_implements(__qin_args[7], "com.slime.ast.AstNode")) && (__qin_args[8] === null || __qin_args[8] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createFunctionDeclaration_9_3(__qin_args[0], __qin_args[1], (Array.isArray(__qin_args[2]) ? new __QinJavaUtilArrayList(__qin_args[2]) : __qin_args[2]), __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    throw new Error("Unsupported Java overload: createFunctionDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createFunctionDeclaration_6_0(id: com_slime_ast_nodes_expressions_Identifier, params: any[], body: any, generator: boolean, async: boolean, location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createFunctionDeclaration(id, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(params, __qin_java_class_info__(null, { name: "com.slime.ast.Pattern", interfaceName: "com.slime.ast.Pattern" })), __QinJavaUtilList.of(), (() => {
      if ((() => { const __qin_instanceof_value = body; return __qin_instanceof_value instanceof com_slime_ast_nodes_statements_BlockStatement; })()) {
        return (body);
      }
      return null;
    })(), generator, async, null, null, location);
  }
  static __qin_overload_createFunctionDeclaration_9_1(id: com_slime_ast_nodes_expressions_Identifier, params: any, parameterMetadata: any, body: com_slime_ast_nodes_statements_BlockStatement, generator: boolean, async: boolean, returnType: com_slime_ast_AstNode, typeParameters: com_slime_ast_AstNode, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_declarations_FunctionDeclaration(id, (() => {
      if (__qin_binary__("==", params, null)) {
        return __QinJavaUtilList.of();
      }
      return params;
    })(), (() => {
      if (__qin_binary__("==", parameterMetadata, null)) {
        return __QinJavaUtilList.of();
      }
      return parameterMetadata;
    })(), body, generator, async, returnType, typeParameters, location);
  }
  static __qin_overload_createFunctionDeclaration_6_2(id: com_slime_ast_nodes_expressions_Identifier, params: com_slime_ast_Pattern[], body: com_slime_ast_nodes_statements_BlockStatement, generator: boolean, async: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createFunctionDeclaration(id, params, null, body, generator, async, null, null, location);
  }
  static __qin_overload_createFunctionDeclaration_9_3(id: com_slime_ast_nodes_expressions_Identifier, params: com_slime_ast_Pattern[], parameterMetadata: any, body: com_slime_ast_nodes_statements_BlockStatement, generator: boolean, async: boolean, returnType: com_slime_ast_AstNode, typeParameters: com_slime_ast_AstNode, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_declarations_FunctionDeclaration(id, (() => {
      if (__qin_binary__("==", params, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(params);
    })(), (() => {
      if (__qin_binary__("==", parameterMetadata, null)) {
        return __QinJavaUtilList.of();
      }
      return parameterMetadata;
    })(), body, generator, async, returnType, typeParameters, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createVariableDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createVariableDeclaration_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createVariableDeclaration_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createVariableDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createVariableDeclaration_3_0(declarations: any[], kind: string, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_declarations_VariableDeclaration(kind, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(declarations, __qin_java_class_info__(com_slime_ast_nodes_misc_VariableDeclarator, { name: "com.slime.ast.nodes.misc.VariableDeclarator" })), location);
  }
  static __qin_overload_createVariableDeclaration_3_1(declarations: com_slime_ast_nodes_misc_VariableDeclarator[], kind: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_declarations_VariableDeclaration(kind, (() => {
      if (__qin_binary__("==", declarations, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(declarations);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createVariableDeclarator(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Pattern")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createVariableDeclarator_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createVariableDeclarator_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Pattern")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createVariableDeclarator_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createVariableDeclarator_3_3(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createVariableDeclarator/" + __qin_args.length);
  }
  static __qin_overload_createVariableDeclarator_3_0(id: com_slime_ast_Pattern, init: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_VariableDeclarator(id, init, location);
  }
  static __qin_overload_createVariableDeclarator_3_1(id: com_slime_ast_nodes_expressions_Identifier, init: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createVariableDeclarator((id), init, location);
  }
  static __qin_overload_createVariableDeclarator_3_2(id: com_slime_ast_Pattern, init: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createVariableDeclarator(id, init, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createVariableDeclarator_3_3(id: com_slime_ast_nodes_expressions_Identifier, init: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createVariableDeclarator((id), init, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createBlockStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createBlockStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createBlockStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createBlockStatement/" + __qin_args.length);
  }
  static __qin_overload_createBlockStatement_2_0(body: com_slime_ast_Statement[], location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_BlockStatement((() => {
      if (__qin_binary__("==", body, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(body);
    })(), location);
  }
  static __qin_overload_createBlockStatement_2_1(body: com_slime_ast_Statement[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_BlockStatement((() => {
      if (__qin_binary__("==", body, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(body);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createExpressionStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExpressionStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExpressionStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createExpressionStatement/" + __qin_args.length);
  }
  static __qin_overload_createExpressionStatement_2_0(expression: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ExpressionStatement(expression, location);
  }
  static __qin_overload_createExpressionStatement_2_1(expression: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_ExpressionStatement(expression, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createIfStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createIfStatement_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createIfStatement_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: createIfStatement/" + __qin_args.length);
  }
  static __qin_overload_createIfStatement_4_0(test: com_slime_ast_Expression, consequent: com_slime_ast_Statement, alternate: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_IfStatement(test, consequent, alternate, location);
  }
  static __qin_overload_createIfStatement_4_1(test: com_slime_ast_Expression, consequent: com_slime_ast_Statement, alternate: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_IfStatement(test, consequent, alternate, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createWhileStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createWhileStatement_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createWhileStatement_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createWhileStatement/" + __qin_args.length);
  }
  static __qin_overload_createWhileStatement_3_0(test: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_WhileStatement(test, body, location);
  }
  static __qin_overload_createWhileStatement_3_1(test: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_WhileStatement(test, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createDoWhileStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Statement")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createDoWhileStatement_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Statement")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createDoWhileStatement_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createDoWhileStatement/" + __qin_args.length);
  }
  static __qin_overload_createDoWhileStatement_3_0(body: com_slime_ast_Statement, test: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_DoWhileStatement(body, test, location);
  }
  static __qin_overload_createDoWhileStatement_3_1(body: com_slime_ast_Statement, test: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_DoWhileStatement(body, test, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createForStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Expression")) && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.slime.ast.Statement")) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SourceLocation || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createForStatement_5_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Expression")) && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.slime.ast.Statement")) && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createForStatement_5_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    throw new Error("Unsupported Java overload: createForStatement/" + __qin_args.length);
  }
  static __qin_overload_createForStatement_5_0(init: any, test: com_slime_ast_Expression, update: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ForStatement((() => {
      if ((() => { const __qin_instanceof_value = init; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (init);
      }
      return null;
    })(), test, update, body, location);
  }
  static __qin_overload_createForStatement_5_1(init: any, test: com_slime_ast_Expression, update: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createForStatement(init, test, update, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createForInStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createForInStatement_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createForInStatement_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: createForInStatement/" + __qin_args.length);
  }
  static __qin_overload_createForInStatement_4_0(left: any, right: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ForInStatement((() => {
      if ((() => { const __qin_instanceof_value = left; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (left);
      }
      return null;
    })(), right, body, location);
  }
  static __qin_overload_createForInStatement_4_1(left: any, right: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createForInStatement(left, right, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createForOfStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SourceLocation || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createForOfStatement_5_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.Statement")) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createForOfStatement_5_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    throw new Error("Unsupported Java overload: createForOfStatement/" + __qin_args.length);
  }
  static __qin_overload_createForOfStatement_5_0(left: any, right: com_slime_ast_Expression, body: com_slime_ast_Statement, __qin_await: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ForOfStatement((() => {
      if ((() => { const __qin_instanceof_value = left; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (left);
      }
      return null;
    })(), right, body, __qin_await, location);
  }
  static __qin_overload_createForOfStatement_5_1(left: any, right: com_slime_ast_Expression, body: com_slime_ast_Statement, __qin_await: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createForOfStatement(left, right, body, __qin_await, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createReturnStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createReturnStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createReturnStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createReturnStatement/" + __qin_args.length);
  }
  static __qin_overload_createReturnStatement_2_0(argument: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ReturnStatement(argument, location);
  }
  static __qin_overload_createReturnStatement_2_1(argument: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_ReturnStatement(argument, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createBreakStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createBreakStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createBreakStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createBreakStatement/" + __qin_args.length);
  }
  static __qin_overload_createBreakStatement_2_0(label: com_slime_ast_nodes_expressions_Identifier, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_BreakStatement(label, location);
  }
  static __qin_overload_createBreakStatement_2_1(label: com_slime_ast_nodes_expressions_Identifier, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_BreakStatement(label, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createContinueStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createContinueStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createContinueStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createContinueStatement/" + __qin_args.length);
  }
  static __qin_overload_createContinueStatement_2_0(label: com_slime_ast_nodes_expressions_Identifier, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ContinueStatement(label, location);
  }
  static __qin_overload_createContinueStatement_2_1(label: com_slime_ast_nodes_expressions_Identifier, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_ContinueStatement(label, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createThrowStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createThrowStatement_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createThrowStatement_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createThrowStatement/" + __qin_args.length);
  }
  static __qin_overload_createThrowStatement_2_0(argument: com_slime_ast_Expression, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_ThrowStatement(argument, location);
  }
  static __qin_overload_createThrowStatement_2_1(argument: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_ThrowStatement(argument, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createTryStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_misc_CatchClause || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_misc_CatchClause.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createTryStatement_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_misc_CatchClause || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_misc_CatchClause.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createTryStatement_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: createTryStatement/" + __qin_args.length);
  }
  static __qin_overload_createTryStatement_4_0(block: com_slime_ast_nodes_statements_BlockStatement, handler: com_slime_ast_nodes_misc_CatchClause, finalizer: com_slime_ast_nodes_statements_BlockStatement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_TryStatement(block, handler, finalizer, location);
  }
  static __qin_overload_createTryStatement_4_1(block: com_slime_ast_nodes_statements_BlockStatement, handler: com_slime_ast_nodes_misc_CatchClause, finalizer: com_slime_ast_nodes_statements_BlockStatement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_TryStatement(block, handler, finalizer, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createCatchClause(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Pattern")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createCatchClause_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Pattern")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createCatchClause_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createCatchClause/" + __qin_args.length);
  }
  static __qin_overload_createCatchClause_3_0(param: com_slime_ast_Pattern, body: com_slime_ast_nodes_statements_BlockStatement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_CatchClause(param, body, location);
  }
  static __qin_overload_createCatchClause_3_1(param: com_slime_ast_Pattern, body: com_slime_ast_nodes_statements_BlockStatement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_CatchClause(param, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createSwitchStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createSwitchStatement_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createSwitchStatement_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createSwitchStatement/" + __qin_args.length);
  }
  static __qin_overload_createSwitchStatement_3_0(discriminant: com_slime_ast_Expression, cases: com_slime_ast_nodes_misc_SwitchCase[], location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_SwitchStatement(discriminant, (() => {
      if (__qin_binary__("==", cases, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(cases);
    })(), location);
  }
  static __qin_overload_createSwitchStatement_3_1(discriminant: com_slime_ast_Expression, cases: com_slime_ast_nodes_misc_SwitchCase[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_SwitchStatement(discriminant, (() => {
      if (__qin_binary__("==", cases, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(cases);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createSwitchCase(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createSwitchCase_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createSwitchCase_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createSwitchCase/" + __qin_args.length);
  }
  static __qin_overload_createSwitchCase_3_0(test: com_slime_ast_Expression, consequent: com_slime_ast_Statement[], location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_SwitchCase(test, (() => {
      if (__qin_binary__("==", consequent, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(consequent);
    })(), location);
  }
  static __qin_overload_createSwitchCase_3_1(test: com_slime_ast_Expression, consequent: com_slime_ast_Statement[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_SwitchCase(test, (() => {
      if (__qin_binary__("==", consequent, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(consequent);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createLabeledStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createLabeledStatement_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createLabeledStatement_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createLabeledStatement/" + __qin_args.length);
  }
  static __qin_overload_createLabeledStatement_3_0(label: com_slime_ast_nodes_expressions_Identifier, body: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_LabeledStatement(label, body, location);
  }
  static __qin_overload_createLabeledStatement_3_1(label: com_slime_ast_nodes_expressions_Identifier, body: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_LabeledStatement(label, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createWithStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createWithStatement_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Statement")) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createWithStatement_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createWithStatement/" + __qin_args.length);
  }
  static __qin_overload_createWithStatement_3_0(object: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_WithStatement(object, body, location);
  }
  static __qin_overload_createWithStatement_3_1(object: com_slime_ast_Expression, body: com_slime_ast_Statement, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_WithStatement(object, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createDebuggerStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_SourceLocation || __qin_args[0].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createDebuggerStatement_1_0(__qin_args[0]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createDebuggerStatement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: createDebuggerStatement/" + __qin_args.length);
  }
  static __qin_overload_createDebuggerStatement_1_0(location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_DebuggerStatement(location);
  }
  static __qin_overload_createDebuggerStatement_1_1(location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_DebuggerStatement(com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createEmptyStatement(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_SourceLocation || __qin_args[0].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createEmptyStatement_1_0(__qin_args[0]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createEmptyStatement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: createEmptyStatement/" + __qin_args.length);
  }
  static __qin_overload_createEmptyStatement_1_0(location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_statements_EmptyStatement(location);
  }
  static __qin_overload_createEmptyStatement_1_1(location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_statements_EmptyStatement(com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createIdentifier(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createIdentifier_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createIdentifier_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createIdentifier/" + __qin_args.length);
  }
  static __qin_overload_createIdentifier_2_0(name: string, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_expressions_Identifier(name, location);
  }
  static __qin_overload_createIdentifier_2_1(name: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_Identifier(name, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createStringLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass) && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_createStringLiteral_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation) && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_createStringLiteral_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createStringLiteral/" + __qin_args.length);
  }
  static __qin_overload_createStringLiteral_3_0(value: string, location: com_slime_ast_SourceLocation, raw: string): any {
    return new com_slime_ast_nodes_expressions_Literal(value, raw, null, null, location);
  }
  static __qin_overload_createStringLiteral_3_1(value: string, location: com_subhuti_struct_SubhutiSourceLocation, raw: string): any {
    return new com_slime_ast_nodes_expressions_Literal(value, raw, null, null, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createRegexLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createRegexLiteral_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createRegexLiteral_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createRegexLiteral/" + __qin_args.length);
  }
  static __qin_overload_createRegexLiteral_2_0(raw: string, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_expressions_Literal(null, raw, __QinJavaUtilRegexPattern.compile(""), null, location);
  }
  static __qin_overload_createRegexLiteral_2_1(raw: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createRegexLiteral(raw, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createBooleanLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "boolean" && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createBooleanLiteral_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && typeof __qin_args[0] === "boolean" && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createBooleanLiteral_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createBooleanLiteral/" + __qin_args.length);
  }
  static __qin_overload_createBooleanLiteral_2_0(value: boolean, location: com_slime_ast_SourceLocation): any {
    return com_slime_ast_nodes_expressions_Literal.bool(value, location);
  }
  static __qin_overload_createBooleanLiteral_2_1(value: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_ast_nodes_expressions_Literal.bool(value, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createNumericLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "number") && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createNumericLiteral_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "number") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createNumericLiteral_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "number") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createNumericLiteral_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createNumericLiteral/" + __qin_args.length);
  }
  static __qin_overload_createNumericLiteral_2_0(value: number, location: com_slime_ast_SourceLocation): any {
    return com_slime_ast_nodes_expressions_Literal.number(value.doubleValue(), location);
  }
  static __qin_overload_createNumericLiteral_3_1(value: number, raw: string, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_expressions_Literal(value, raw, null, null, location);
  }
  static __qin_overload_createNumericLiteral_3_2(value: number, raw: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_Literal(value, raw, null, null, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createNullLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_SourceLocation || __qin_args[0].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createNullLiteral_1_0(__qin_args[0]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_args[0] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createNullLiteral_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: createNullLiteral/" + __qin_args.length);
  }
  static __qin_overload_createNullLiteral_1_0(location: com_slime_ast_SourceLocation): any {
    return com_slime_ast_nodes_expressions_Literal.nullLiteral(location);
  }
  static __qin_overload_createNullLiteral_1_1(location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_ast_nodes_expressions_Literal.nullLiteral(com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createTemplateLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && true && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createTemplateLiteral_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && true && true && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createTemplateLiteral_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createTemplateLiteral/" + __qin_args.length);
  }
  static __qin_overload_createTemplateLiteral_3_0(quasis: any[], expressions: com_slime_ast_Expression[], location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_expressions_TemplateLiteral(com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(quasis, __qin_java_class_info__(com_slime_ast_nodes_misc_TemplateElement, { name: "com.slime.ast.nodes.misc.TemplateElement" })), (() => {
      if (__qin_binary__("==", expressions, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(expressions);
    })(), location);
  }
  static __qin_overload_createTemplateLiteral_3_1(quasis: any[], expressions: com_slime_ast_Expression[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createTemplateLiteral(quasis, expressions, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createTemplateElement(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createTemplateElement_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createTemplateElement_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: createTemplateElement/" + __qin_args.length);
  }
  static __qin_overload_createTemplateElement_4_0(raw: string, cooked: string, tail: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_TemplateElement(raw, cooked, tail, location);
  }
  static __qin_overload_createTemplateElement_4_1(raw: string, cooked: string, tail: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createTemplateElement(raw, cooked, tail, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createTaggedTemplateExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_TemplateLiteral || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_TemplateLiteral.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createTaggedTemplateExpression_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_TemplateLiteral || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_TemplateLiteral.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createTaggedTemplateExpression_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createTaggedTemplateExpression/" + __qin_args.length);
  }
  static __qin_overload_createTaggedTemplateExpression_3_0(tag: com_slime_ast_Expression, quasi: com_slime_ast_nodes_expressions_TemplateLiteral, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_expressions_TaggedTemplateExpression(tag, quasi, location);
  }
  static __qin_overload_createTaggedTemplateExpression_3_1(tag: com_slime_ast_Expression, quasi: com_slime_ast_nodes_expressions_TemplateLiteral, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createTaggedTemplateExpression(tag, quasi, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createProperty(...__qin_args: any[]): any {
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.AstNode")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.AstNode")) && (__qin_args[2] === null || typeof __qin_args[2] === "string") && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SourceLocation || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createProperty_7_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.AstNode")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.AstNode")) && (__qin_args[2] === null || typeof __qin_args[2] === "string") && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createProperty_7_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || typeof __qin_args[2] === "string") && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SourceLocation || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createProperty_7_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.slime.ast.AstNode")) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createProperty_7_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6]);
    throw new Error("Unsupported Java overload: createProperty/" + __qin_args.length);
  }
  static __qin_overload_createProperty_7_0(key: com_slime_ast_AstNode, value: com_slime_ast_AstNode, kind: string, computed: boolean, shorthand: boolean, method: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_Property(key, value, kind, method, shorthand, computed, location);
  }
  static __qin_overload_createProperty_7_1(key: com_slime_ast_AstNode, value: com_slime_ast_AstNode, kind: string, computed: boolean, shorthand: boolean, method: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createProperty(key, value, kind, computed, shorthand, method, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createProperty_7_2(key: com_slime_ast_Expression, value: com_slime_ast_Expression, kind: string, computed: boolean, shorthand: boolean, method: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_Property(key, value, kind, method, shorthand, computed, location);
  }
  static __qin_overload_createProperty_7_3(kind: string, key: com_slime_ast_Expression, value: com_slime_ast_AstNode, method: boolean, shorthand: boolean, computed: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_Property(key, value, kind, method, shorthand, computed, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createImportDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createImportDeclaration_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createImportDeclaration_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 8 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SyntaxToken || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SourceLocation || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createImportDeclaration_8_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7]);
    if (__qin_args.length === 9 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_slime_ast_SourceLocation || __qin_args[8].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createImportDeclaration_9_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createImportDeclaration_3_4(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createImportDeclaration_4_5(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 8 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SyntaxToken || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createImportDeclaration_8_6(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7]);
    if (__qin_args.length === 9 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[1].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createImportDeclaration_9_7(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    throw new Error("Unsupported Java overload: createImportDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createImportDeclaration_3_0(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, false, location);
  }
  static __qin_overload_createImportDeclaration_4_1(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ImportDeclaration(source, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(specifiers, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), null, null, null, null, null, null, typeOnly, location);
  }
  static __qin_overload_createImportDeclaration_8_2(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, importToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, false, importToken, fromToken, lBraceToken, rBraceToken, semicolonToken, location);
  }
  static __qin_overload_createImportDeclaration_9_3(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, importToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ImportDeclaration(source, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(specifiers, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), importToken, fromToken, lBraceToken, rBraceToken, semicolonToken, location);
  }
  static __qin_overload_createImportDeclaration_3_4(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createImportDeclaration_4_5(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, typeOnly, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createImportDeclaration_8_6(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, importToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, importToken, fromToken, lBraceToken, rBraceToken, semicolonToken, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createImportDeclaration_9_7(specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, importToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createImportDeclaration(specifiers, source, typeOnly, importToken, fromToken, lBraceToken, rBraceToken, semicolonToken, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createExportNamedDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SourceLocation || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportNamedDeclaration_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SourceLocation || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportNamedDeclaration_5_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_slime_ast_SourceLocation || __qin_args[8].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportNamedDeclaration_9_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    if (__qin_args.length === 10 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_slime_ast_SyntaxToken || __qin_args[8].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[9] === null || __qin_args[9] instanceof com_slime_ast_SourceLocation || __qin_args[9].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportNamedDeclaration_10_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8], __qin_args[9]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportNamedDeclaration_4_4(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportNamedDeclaration_5_5(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportNamedDeclaration_9_6(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    if (__qin_args.length === 10 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Declaration")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_Literal || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Literal.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SyntaxToken || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[5] === null || __qin_args[5] instanceof com_slime_ast_SyntaxToken || __qin_args[5].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[6] === null || __qin_args[6] instanceof com_slime_ast_SyntaxToken || __qin_args[6].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[7] === null || __qin_args[7] instanceof com_slime_ast_SyntaxToken || __qin_args[7].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[8] === null || __qin_args[8] instanceof com_slime_ast_SyntaxToken || __qin_args[8].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[9] === null || __qin_args[9] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportNamedDeclaration_10_7(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8], __qin_args[9]);
    throw new Error("Unsupported Java overload: createExportNamedDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createExportNamedDeclaration_4_0(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ExportNamedDeclaration(declaration, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(specifiers, __qin_java_class_info__(com_slime_ast_nodes_misc_ExportSpecifier, { name: "com.slime.ast.nodes.misc.ExportSpecifier" })), source, location);
  }
  static __qin_overload_createExportNamedDeclaration_5_1(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ExportNamedDeclaration(declaration, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(specifiers, __qin_java_class_info__(com_slime_ast_nodes_misc_ExportSpecifier, { name: "com.slime.ast.nodes.misc.ExportSpecifier" })), source, typeOnly, location);
  }
  static __qin_overload_createExportNamedDeclaration_9_2(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, exportToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_slime_ast_SourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportNamedDeclaration(declaration, specifiers, source, false, exportToken, fromToken, lBraceToken, rBraceToken, semicolonToken, location);
  }
  static __qin_overload_createExportNamedDeclaration_10_3(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, exportToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ExportNamedDeclaration(declaration, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(specifiers, __qin_java_class_info__(com_slime_ast_nodes_misc_ExportSpecifier, { name: "com.slime.ast.nodes.misc.ExportSpecifier" })), source, typeOnly, exportToken, fromToken, lBraceToken, rBraceToken, semicolonToken, location);
  }
  static __qin_overload_createExportNamedDeclaration_4_4(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportNamedDeclaration(declaration, specifiers, source, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createExportNamedDeclaration_5_5(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportNamedDeclaration(declaration, specifiers, source, typeOnly, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createExportNamedDeclaration_9_6(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, exportToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportNamedDeclaration(declaration, specifiers, source, false, exportToken, fromToken, lBraceToken, rBraceToken, semicolonToken, location);
  }
  static __qin_overload_createExportNamedDeclaration_10_7(declaration: com_slime_ast_Declaration, specifiers: any[], source: com_slime_ast_nodes_expressions_Literal, typeOnly: boolean, exportToken: com_slime_ast_SyntaxToken, fromToken: com_slime_ast_SyntaxToken, lBraceToken: com_slime_ast_SyntaxToken, rBraceToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportNamedDeclaration(declaration, specifiers, source, typeOnly, exportToken, fromToken, lBraceToken, rBraceToken, semicolonToken, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createExportDefaultDeclaration(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportDefaultDeclaration_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SyntaxToken || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SyntaxToken || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_slime_ast_SourceLocation || __qin_args[4].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createExportDefaultDeclaration_5_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportDefaultDeclaration_2_2(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SyntaxToken || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SyntaxToken || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_SyntaxToken || __qin_args[3].__qinJavaRecordClass === com_slime_ast_SyntaxToken.__qinJavaRecordClass) && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createExportDefaultDeclaration_5_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    throw new Error("Unsupported Java overload: createExportDefaultDeclaration/" + __qin_args.length);
  }
  static __qin_overload_createExportDefaultDeclaration_2_0(declaration: any, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ExportDefaultDeclaration((() => {
      if ((() => { const __qin_instanceof_value = declaration; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (declaration);
      }
      return null;
    })(), location);
  }
  static __qin_overload_createExportDefaultDeclaration_5_1(declaration: any, exportToken: com_slime_ast_SyntaxToken, defaultToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_modules_ExportDefaultDeclaration((() => {
      if ((() => { const __qin_instanceof_value = declaration; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (declaration);
      }
      return null;
    })(), exportToken, defaultToken, location, semicolonToken);
  }
  static __qin_overload_createExportDefaultDeclaration_2_2(declaration: any, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportDefaultDeclaration(declaration, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createExportDefaultDeclaration_5_3(declaration: any, exportToken: com_slime_ast_SyntaxToken, defaultToken: com_slime_ast_SyntaxToken, semicolonToken: com_slime_ast_SyntaxToken, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createExportDefaultDeclaration(declaration, exportToken, defaultToken, semicolonToken, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createProgram(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_SourceLocation || __qin_args[2].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createProgram_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && true && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createProgram_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: createProgram/" + __qin_args.length);
  }
  static __qin_overload_createProgram_3_0(body: any[], sourceType: string, location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_Program(sourceType, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(body, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), location);
  }
  static __qin_overload_createProgram_3_1(body: any[], sourceType: string, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createProgram(body, sourceType, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createClassBody(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_slime_ast_SourceLocation || __qin_args[1].__qinJavaRecordClass === com_slime_ast_SourceLocation.__qinJavaRecordClass)) return this.__qin_overload_createClassBody_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassBody_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createClassBody/" + __qin_args.length);
  }
  static __qin_overload_createClassBody_2_0(body: any[], location: com_slime_ast_SourceLocation): any {
    return new com_slime_ast_nodes_misc_ClassBody(com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(body, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), location);
  }
  static __qin_overload_createClassBody_2_1(body: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_ClassBody(com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(body, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createArrayExpression(elements: com_slime_ast_Expression[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ArrayExpression((() => {
      if (__qin_binary__("==", elements, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(elements);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createObjectExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createObjectExpression_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 2 && true && (__qin_args[1] === null || __qin_args[1] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createObjectExpression_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: createObjectExpression/" + __qin_args.length);
  }
  static __qin_overload_createObjectExpression_2_0(properties: com_slime_ast_AstNode[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ObjectExpression((() => {
      if (__qin_binary__("==", properties, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(properties);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static __qin_overload_createObjectExpression_2_1(properties: com_slime_ast_nodes_misc_Property[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createObjectExpression((properties), location);
  }
  static createBinaryExpression(operator: string, left: com_slime_ast_Expression, right: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_BinaryExpression(operator, left, right, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createLogicalExpression(operator: string, left: com_slime_ast_Expression, right: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_LogicalExpression(operator, left, right, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createAssignmentExpression(operator: string, left: com_slime_ast_Expression, right: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_AssignmentExpression(operator, (() => {
      if ((() => { const __qin_instanceof_value = left; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (left);
      }
      return com_slime_parser_cstToAst_SlimeAstCreateUtils.toPattern(left, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
    })(), right, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createUnaryExpression(operator: string, prefix: boolean, argument: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_UnaryExpression(operator, argument, prefix, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createUpdateExpression(operator: string, argument: com_slime_ast_Expression, prefix: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_UpdateExpression(operator, argument, prefix, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createSequenceExpression(expressions: com_slime_ast_Expression[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_SequenceExpression((() => {
      if (__qin_binary__("==", expressions, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(expressions);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createParenthesizedExpression(expression: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ParenthesizedExpression(expression, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createConditionalExpression(test: com_slime_ast_Expression, consequent: com_slime_ast_Expression, alternate: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ConditionalExpression(test, consequent, alternate, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createCallExpression(callee: com_slime_ast_Expression, __qin_arguments: com_slime_ast_Expression[], optional: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_CallExpression(callee, (() => {
      if (__qin_binary__("==", __qin_arguments, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(__qin_arguments);
    })(), optional, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createNewExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createNewExpression_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_java_implements(__qin_args[0], "com.slime.ast.Expression")) && true && typeof __qin_args[2] === "boolean" && (__qin_args[3] === null || __qin_args[3] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createNewExpression_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: createNewExpression/" + __qin_args.length);
  }
  static __qin_overload_createNewExpression_3_0(callee: com_slime_ast_Expression, __qin_arguments: com_slime_ast_Expression[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createNewExpression(callee, __qin_arguments, (() => {
      if (__qin_binary__("!=", __qin_arguments, null)) {
        return __qin_binary__(">", __qin_arguments.length, 0.0);
      }
      return false;
    })(), location);
  }
  static __qin_overload_createNewExpression_4_1(callee: com_slime_ast_Expression, __qin_arguments: com_slime_ast_Expression[], hasArguments: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_NewExpression(callee, (() => {
      if (__qin_binary__("==", __qin_arguments, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(__qin_arguments);
    })(), hasArguments, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createMemberExpression(object: com_slime_ast_Expression, property: com_slime_ast_Expression, computed: boolean, optional: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_MemberExpression(object, property, computed, optional, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createYieldExpression(argument: com_slime_ast_Expression, delegate: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_YieldExpression(argument, delegate, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createAwaitExpression(argument: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_AwaitExpression(argument, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createThisExpression(location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ThisExpression(com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createSpreadElement(argument: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_SpreadElement(argument, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createRestElement(argument: com_slime_ast_Pattern, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_patterns_RestElement(argument, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createArrayPattern(elements: com_slime_ast_Pattern[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_patterns_ArrayPattern((() => {
      if (__qin_binary__("==", elements, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(elements);
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createObjectPattern(properties: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_patterns_ObjectPattern(com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(properties, __qin_java_class_info__(null, { name: "com.slime.ast.AstNode", interfaceName: "com.slime.ast.AstNode" })), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createAssignmentPattern(left: com_slime_ast_Pattern, right: com_slime_ast_Expression, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_patterns_AssignmentPattern(left, right, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createFunctionExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 6 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && true && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || __qin_args[5] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createFunctionExpression_6_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && true && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_args[2] instanceof __QinJavaUtilArrayList || __qin_args[2] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[3] === null || __qin_args[3] instanceof com_slime_ast_nodes_statements_BlockStatement || __qin_args[3].__qinJavaRecordClass === com_slime_ast_nodes_statements_BlockStatement.__qinJavaRecordClass) && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || __qin_java_implements(__qin_args[6], "com.slime.ast.AstNode")) && (__qin_args[7] === null || __qin_java_implements(__qin_args[7], "com.slime.ast.AstNode")) && (__qin_args[8] === null || __qin_args[8] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createFunctionExpression_9_1(__qin_args[0], __qin_args[1], (Array.isArray(__qin_args[2]) ? new __QinJavaUtilArrayList(__qin_args[2]) : __qin_args[2]), __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8]);
    throw new Error("Unsupported Java overload: createFunctionExpression/" + __qin_args.length);
  }
  static __qin_overload_createFunctionExpression_6_0(id: com_slime_ast_nodes_expressions_Identifier, params: com_slime_ast_Pattern[], body: com_slime_ast_nodes_statements_BlockStatement, generator: boolean, async: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createFunctionExpression(id, params, null, body, generator, async, null, null, location);
  }
  static __qin_overload_createFunctionExpression_9_1(id: com_slime_ast_nodes_expressions_Identifier, params: com_slime_ast_Pattern[], parameterMetadata: any, body: com_slime_ast_nodes_statements_BlockStatement, generator: boolean, async: boolean, returnType: com_slime_ast_AstNode, typeParameters: com_slime_ast_AstNode, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_FunctionExpression(id, (() => {
      if (__qin_binary__("==", params, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(params);
    })(), (() => {
      if (__qin_binary__("==", parameterMetadata, null)) {
        return __QinJavaUtilList.of();
      }
      return parameterMetadata;
    })(), body, generator, async, returnType, typeParameters, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createFunctionParameter(pattern: com_slime_ast_Pattern, decorators: any, typeAnnotation: com_slime_ast_AstNode, optional: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_FunctionParameter(pattern, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), typeAnnotation, optional, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createClassExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 5 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && true && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassExpression_5_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.slime.ast.AstNode")) && (__qin_args[4] === null || Array.isArray(__qin_args[4]) || __qin_args[4] instanceof __QinJavaUtilArrayList || __qin_args[4] instanceof __QinJavaUtilUnmodifiableList) && true && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassExpression_7_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], (Array.isArray(__qin_args[4]) ? new __QinJavaUtilArrayList(__qin_args[4]) : __qin_args[4]), __qin_args[5], __qin_args[6]);
    if (__qin_args.length === 7 && (__qin_args[0] === null || __qin_args[0] instanceof com_slime_ast_nodes_expressions_Identifier || __qin_args[0].__qinJavaRecordClass === com_slime_ast_nodes_expressions_Identifier.__qinJavaRecordClass) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_misc_ClassBody || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_misc_ClassBody.__qinJavaRecordClass) && (__qin_args[3] === null || Array.isArray(__qin_args[3]) || __qin_args[3] instanceof __QinJavaUtilArrayList || __qin_args[3] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[4] === null || __qin_java_implements(__qin_args[4], "com.slime.ast.AstNode")) && (__qin_args[5] === null || Array.isArray(__qin_args[5]) || __qin_args[5] instanceof __QinJavaUtilArrayList || __qin_args[5] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[6] === null || __qin_args[6] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createClassExpression_7_2(__qin_args[0], __qin_args[1], __qin_args[2], (Array.isArray(__qin_args[3]) ? new __QinJavaUtilArrayList(__qin_args[3]) : __qin_args[3]), __qin_args[4], (Array.isArray(__qin_args[5]) ? new __QinJavaUtilArrayList(__qin_args[5]) : __qin_args[5]), __qin_args[6]);
    throw new Error("Unsupported Java overload: createClassExpression/" + __qin_args.length);
  }
  static __qin_overload_createClassExpression_5_0(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassExpression(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), null, __QinJavaUtilList.of(), location);
  }
  static __qin_overload_createClassExpression_7_1(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, typeParameters: com_slime_ast_AstNode, implementsTypes: any, decorators: any[], location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createClassExpression(id, superClass, body, com_slime_parser_cstToAst_SlimeAstCreateUtils.filterList(decorators, __qin_java_class_info__(com_slime_ast_nodes_misc_Decorator, { name: "com.slime.ast.nodes.misc.Decorator" })), typeParameters, implementsTypes, location);
  }
  static __qin_overload_createClassExpression_7_2(id: com_slime_ast_nodes_expressions_Identifier, superClass: com_slime_ast_Expression, body: com_slime_ast_nodes_misc_ClassBody, decorators: any, typeParameters: com_slime_ast_AstNode, implementsTypes: any, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ClassExpression(id, superClass, body, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), typeParameters, (() => {
      if (__qin_binary__("==", implementsTypes, null)) {
        return __QinJavaUtilList.of();
      }
      return implementsTypes;
    })(), com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createMethodDefinition(...__qin_args: any[]): any {
    if (__qin_args.length === 6 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof Object) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_FunctionExpression || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_FunctionExpression.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || __qin_args[5] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createMethodDefinition_6_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    if (__qin_args.length === 8 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof Object) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_FunctionExpression || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_FunctionExpression.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || typeof __qin_args[5] === "string") && typeof __qin_args[6] === "boolean" && (__qin_args[7] === null || __qin_args[7] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createMethodDefinition_8_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7]);
    if (__qin_args.length === 9 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_args[1] instanceof Object) && (__qin_args[2] === null || __qin_args[2] instanceof com_slime_ast_nodes_expressions_FunctionExpression || __qin_args[2].__qinJavaRecordClass === com_slime_ast_nodes_expressions_FunctionExpression.__qinJavaRecordClass) && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || Array.isArray(__qin_args[5]) || __qin_args[5] instanceof __QinJavaUtilArrayList || __qin_args[5] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[6] === null || typeof __qin_args[6] === "string") && typeof __qin_args[7] === "boolean" && (__qin_args[8] === null || __qin_args[8] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createMethodDefinition_9_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], (Array.isArray(__qin_args[5]) ? new __QinJavaUtilArrayList(__qin_args[5]) : __qin_args[5]), __qin_args[6], __qin_args[7], __qin_args[8]);
    throw new Error("Unsupported Java overload: createMethodDefinition/" + __qin_args.length);
  }
  static __qin_overload_createMethodDefinition_6_0(kind: string, key: any, value: com_slime_ast_nodes_expressions_FunctionExpression, computed: boolean, isStatic: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createMethodDefinition(kind, key, value, computed, isStatic, __QinJavaUtilList.of(), null, false, location);
  }
  static __qin_overload_createMethodDefinition_8_1(kind: string, key: any, value: com_slime_ast_nodes_expressions_FunctionExpression, computed: boolean, isStatic: boolean, accessibility: string, override: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createMethodDefinition(kind, key, value, computed, isStatic, __QinJavaUtilList.of(), accessibility, override, location);
  }
  static __qin_overload_createMethodDefinition_9_2(kind: string, key: any, value: com_slime_ast_nodes_expressions_FunctionExpression, computed: boolean, isStatic: boolean, decorators: any, accessibility: string, override: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_MethodDefinition((() => {
      if ((() => { const __qin_instanceof_value = key; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (key);
      }
      return null;
    })(), value, kind, computed, isStatic, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), accessibility, override, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createPropertyDefinition(...__qin_args: any[]): any {
    if (__qin_args.length === 10 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_java_implements(__qin_args[4], "com.slime.ast.AstNode")) && typeof __qin_args[5] === "boolean" && (__qin_args[6] === null || typeof __qin_args[6] === "string") && typeof __qin_args[7] === "boolean" && typeof __qin_args[8] === "boolean" && (__qin_args[9] === null || __qin_args[9] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createPropertyDefinition_10_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8], __qin_args[9]);
    if (__qin_args.length === 11 && (__qin_args[0] === null || __qin_args[0] instanceof Object) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.slime.ast.Expression")) && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || Array.isArray(__qin_args[4]) || __qin_args[4] instanceof __QinJavaUtilArrayList || __qin_args[4] instanceof __QinJavaUtilUnmodifiableList) && (__qin_args[5] === null || __qin_java_implements(__qin_args[5], "com.slime.ast.AstNode")) && typeof __qin_args[6] === "boolean" && (__qin_args[7] === null || typeof __qin_args[7] === "string") && typeof __qin_args[8] === "boolean" && typeof __qin_args[9] === "boolean" && (__qin_args[10] === null || __qin_args[10] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createPropertyDefinition_11_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], (Array.isArray(__qin_args[4]) ? new __QinJavaUtilArrayList(__qin_args[4]) : __qin_args[4]), __qin_args[5], __qin_args[6], __qin_args[7], __qin_args[8], __qin_args[9], __qin_args[10]);
    throw new Error("Unsupported Java overload: createPropertyDefinition/" + __qin_args.length);
  }
  static __qin_overload_createPropertyDefinition_10_0(key: any, value: com_slime_ast_Expression, computed: boolean, isStatic: boolean, typeAnnotation: com_slime_ast_AstNode, optional: boolean, accessibility: string, readonly: boolean, override: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createPropertyDefinition(key, value, computed, isStatic, __QinJavaUtilList.of(), typeAnnotation, optional, accessibility, readonly, override, location);
  }
  static __qin_overload_createPropertyDefinition_11_1(key: any, value: com_slime_ast_Expression, computed: boolean, isStatic: boolean, decorators: any, typeAnnotation: com_slime_ast_AstNode, optional: boolean, accessibility: string, readonly: boolean, override: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_misc_PropertyDefinition((() => {
      if ((() => { const __qin_instanceof_value = key; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (key);
      }
      return null;
    })(), value, computed, isStatic, (() => {
      if (__qin_binary__("==", decorators, null)) {
        return __QinJavaUtilList.of();
      }
      return decorators;
    })(), typeAnnotation, optional, accessibility, readonly, override, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
  static createArrowFunctionExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 5 && true && (__qin_args[1] === null || __qin_args[1] instanceof Object) && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean" && (__qin_args[4] === null || __qin_args[4] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createArrowFunctionExpression_5_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 6 && true && (__qin_args[1] === null || __qin_args[1] instanceof Object) && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean" && typeof __qin_args[4] === "boolean" && (__qin_args[5] === null || __qin_args[5] instanceof com_subhuti_struct_SubhutiSourceLocation)) return this.__qin_overload_createArrowFunctionExpression_6_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    throw new Error("Unsupported Java overload: createArrowFunctionExpression/" + __qin_args.length);
  }
  static __qin_overload_createArrowFunctionExpression_5_0(params: com_slime_ast_Pattern[], body: any, expression: boolean, async: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createArrowFunctionExpression(params, body, expression, async, false, location);
  }
  static __qin_overload_createArrowFunctionExpression_6_1(params: com_slime_ast_Pattern[], body: any, expression: boolean, async: boolean, paramsParenthesized: boolean, location: com_subhuti_struct_SubhutiSourceLocation): any {
    return new com_slime_ast_nodes_expressions_ArrowFunctionExpression((() => {
      if (__qin_binary__("==", params, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilArrays.asList(params);
    })(), (() => {
      if ((() => { const __qin_instanceof_value = body; return __qin_java_implements(__qin_instanceof_value, "com.slime.ast.AstNode"); })()) {
        return (body);
      }
      return null;
    })(), async, expression, paramsParenthesized, com_slime_parser_cstToAst_SlimeAstCreateUtils.toSourceLocation(location));
  }
}
const SlimeAstCreateUtils = com_slime_parser_cstToAst_SlimeAstCreateUtils;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_cstToAst_SlimeAstCreateUtils };
