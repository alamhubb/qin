import { com_slime_java_JavaParser, com_slime_java_JavaParser as JavaParser } from "../JavaParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../../subhuti/struct/SubhutiCst.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../../subhuti/struct/SubhutiMatchToken.ts";
import { com_slime_java_ast_JavaAstProgram, com_slime_java_ast_JavaAstProgram as JavaAstProgram } from "./JavaAstProgram.ts";
import { com_slime_java_ast_JavaAstImportDeclaration, com_slime_java_ast_JavaAstImportDeclaration as JavaAstImportDeclaration } from "./JavaAstImportDeclaration.ts";
import { com_slime_java_ast_JavaAstClassDeclaration, com_slime_java_ast_JavaAstClassDeclaration as JavaAstClassDeclaration } from "./JavaAstClassDeclaration.ts";
import { com_slime_java_ast_JavaAstStaticInitializer, com_slime_java_ast_JavaAstStaticInitializer as JavaAstStaticInitializer } from "./JavaAstStaticInitializer.ts";
import { com_slime_java_ast_JavaAstFieldDeclaration, com_slime_java_ast_JavaAstFieldDeclaration as JavaAstFieldDeclaration } from "./JavaAstFieldDeclaration.ts";
import { com_slime_java_ast_JavaAstMethodDeclaration, com_slime_java_ast_JavaAstMethodDeclaration as JavaAstMethodDeclaration } from "./JavaAstMethodDeclaration.ts";
import { com_slime_java_ast_JavaAstNewExpression, com_slime_java_ast_JavaAstNewExpression as JavaAstNewExpression } from "./JavaAstNewExpression.ts";
import { com_slime_java_ast_JavaAstTypeParameter, com_slime_java_ast_JavaAstTypeParameter as JavaAstTypeParameter } from "./JavaAstTypeParameter.ts";
import { com_slime_java_ast_JavaAstParameter, com_slime_java_ast_JavaAstParameter as JavaAstParameter } from "./JavaAstParameter.ts";
import { com_slime_java_ast_JavaAstStatement, com_slime_java_ast_JavaAstStatement as JavaAstStatement } from "./JavaAstStatement.ts";
import { com_slime_java_ast_JavaAstExpression, com_slime_java_ast_JavaAstExpression as JavaAstExpression } from "./JavaAstExpression.ts";
import { com_slime_java_ast_JavaAstExpressionStatement, com_slime_java_ast_JavaAstExpressionStatement as JavaAstExpressionStatement } from "./JavaAstExpressionStatement.ts";
import { com_slime_java_ast_JavaAstMethodCallExpression, com_slime_java_ast_JavaAstMethodCallExpression as JavaAstMethodCallExpression } from "./JavaAstMethodCallExpression.ts";
import { com_slime_java_ast_JavaAstReturnStatement, com_slime_java_ast_JavaAstReturnStatement as JavaAstReturnStatement } from "./JavaAstReturnStatement.ts";
import { com_slime_java_ast_JavaAstThrowStatement, com_slime_java_ast_JavaAstThrowStatement as JavaAstThrowStatement } from "./JavaAstThrowStatement.ts";
import { com_slime_java_ast_JavaAstYieldStatement, com_slime_java_ast_JavaAstYieldStatement as JavaAstYieldStatement } from "./JavaAstYieldStatement.ts";
import { com_slime_java_ast_JavaAstBreakStatement, com_slime_java_ast_JavaAstBreakStatement as JavaAstBreakStatement } from "./JavaAstBreakStatement.ts";
import { com_slime_java_ast_JavaAstContinueStatement, com_slime_java_ast_JavaAstContinueStatement as JavaAstContinueStatement } from "./JavaAstContinueStatement.ts";
import { com_slime_java_ast_JavaAstIfStatement, com_slime_java_ast_JavaAstIfStatement as JavaAstIfStatement } from "./JavaAstIfStatement.ts";
import { com_slime_java_ast_JavaAstWhileStatement, com_slime_java_ast_JavaAstWhileStatement as JavaAstWhileStatement } from "./JavaAstWhileStatement.ts";
import { com_slime_java_ast_JavaAstDoWhileStatement, com_slime_java_ast_JavaAstDoWhileStatement as JavaAstDoWhileStatement } from "./JavaAstDoWhileStatement.ts";
import { com_slime_java_ast_JavaAstTryStatement, com_slime_java_ast_JavaAstTryStatement as JavaAstTryStatement } from "./JavaAstTryStatement.ts";
import { com_slime_java_ast_JavaAstSwitchStatement, com_slime_java_ast_JavaAstSwitchStatement as JavaAstSwitchStatement } from "./JavaAstSwitchStatement.ts";
import { com_slime_java_ast_JavaAstSwitchCase, com_slime_java_ast_JavaAstSwitchCase as JavaAstSwitchCase } from "./JavaAstSwitchCase.ts";
import { com_slime_java_ast_JavaAstSwitchExpression, com_slime_java_ast_JavaAstSwitchExpression as JavaAstSwitchExpression } from "./JavaAstSwitchExpression.ts";
import { com_slime_java_ast_JavaAstCatchClause, com_slime_java_ast_JavaAstCatchClause as JavaAstCatchClause } from "./JavaAstCatchClause.ts";
import { com_slime_java_ast_JavaAstEnhancedForStatement, com_slime_java_ast_JavaAstEnhancedForStatement as JavaAstEnhancedForStatement } from "./JavaAstEnhancedForStatement.ts";
import { com_slime_java_ast_JavaAstForStatement, com_slime_java_ast_JavaAstForStatement as JavaAstForStatement } from "./JavaAstForStatement.ts";
import { com_slime_java_ast_JavaAstLocalVariableDeclaration, com_slime_java_ast_JavaAstLocalVariableDeclaration as JavaAstLocalVariableDeclaration } from "./JavaAstLocalVariableDeclaration.ts";
import { com_slime_java_ast_JavaAstArrayLiteralExpression, com_slime_java_ast_JavaAstArrayLiteralExpression as JavaAstArrayLiteralExpression } from "./JavaAstArrayLiteralExpression.ts";
import { com_slime_java_ast_JavaAstIdentifierExpression, com_slime_java_ast_JavaAstIdentifierExpression as JavaAstIdentifierExpression } from "./JavaAstIdentifierExpression.ts";
import { com_slime_java_ast_JavaAstNumberLiteral, com_slime_java_ast_JavaAstNumberLiteral as JavaAstNumberLiteral } from "./JavaAstNumberLiteral.ts";
import { com_slime_java_ast_JavaAstStringLiteral, com_slime_java_ast_JavaAstStringLiteral as JavaAstStringLiteral } from "./JavaAstStringLiteral.ts";
import { com_slime_java_ast_JavaAstBooleanLiteral, com_slime_java_ast_JavaAstBooleanLiteral as JavaAstBooleanLiteral } from "./JavaAstBooleanLiteral.ts";
import { com_slime_java_ast_JavaAstNullLiteral, com_slime_java_ast_JavaAstNullLiteral as JavaAstNullLiteral } from "./JavaAstNullLiteral.ts";
import { com_slime_java_ast_JavaAstThisExpression, com_slime_java_ast_JavaAstThisExpression as JavaAstThisExpression } from "./JavaAstThisExpression.ts";
import { com_slime_java_ast_JavaAstConditionalExpression, com_slime_java_ast_JavaAstConditionalExpression as JavaAstConditionalExpression } from "./JavaAstConditionalExpression.ts";
import { com_slime_java_ast_JavaAstAssignmentExpression, com_slime_java_ast_JavaAstAssignmentExpression as JavaAstAssignmentExpression } from "./JavaAstAssignmentExpression.ts";
import { com_slime_java_ast_JavaAstUnaryExpression, com_slime_java_ast_JavaAstUnaryExpression as JavaAstUnaryExpression } from "./JavaAstUnaryExpression.ts";
import { com_slime_java_ast_JavaAstCastExpression, com_slime_java_ast_JavaAstCastExpression as JavaAstCastExpression } from "./JavaAstCastExpression.ts";
import { com_slime_java_ast_JavaAstMethodReferenceExpression, com_slime_java_ast_JavaAstMethodReferenceExpression as JavaAstMethodReferenceExpression } from "./JavaAstMethodReferenceExpression.ts";
import { com_slime_java_ast_JavaAstClassLiteralExpression, com_slime_java_ast_JavaAstClassLiteralExpression as JavaAstClassLiteralExpression } from "./JavaAstClassLiteralExpression.ts";
import { com_slime_java_ast_JavaAstLambdaExpression, com_slime_java_ast_JavaAstLambdaExpression as JavaAstLambdaExpression } from "./JavaAstLambdaExpression.ts";
import { com_slime_java_ast_JavaAstBinaryExpression, com_slime_java_ast_JavaAstBinaryExpression as JavaAstBinaryExpression } from "./JavaAstBinaryExpression.ts";
import { com_slime_java_ast_JavaAstInstanceofPatternExpression, com_slime_java_ast_JavaAstInstanceofPatternExpression as JavaAstInstanceofPatternExpression } from "./JavaAstInstanceofPatternExpression.ts";
import { com_slime_java_ast_JavaAstInstanceofExpression, com_slime_java_ast_JavaAstInstanceofExpression as JavaAstInstanceofExpression } from "./JavaAstInstanceofExpression.ts";
import { com_slime_java_ast_JavaAstUpdateExpression, com_slime_java_ast_JavaAstUpdateExpression as JavaAstUpdateExpression } from "./JavaAstUpdateExpression.ts";
import { com_slime_java_ast_JavaAstMemberAccessExpression, com_slime_java_ast_JavaAstMemberAccessExpression as JavaAstMemberAccessExpression } from "./JavaAstMemberAccessExpression.ts";
import { com_slime_java_ast_JavaAstArrayAccessExpression, com_slime_java_ast_JavaAstArrayAccessExpression as JavaAstArrayAccessExpression } from "./JavaAstArrayAccessExpression.ts";
import { com_slime_java_ast_JavaAstAnnotation, com_slime_java_ast_JavaAstAnnotation as JavaAstAnnotation } from "./JavaAstAnnotation.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaLangBoolean, __QinJavaLangDouble, __QinJavaLangCharacter, __QinJavaLangStringBuilder, __QinJavaLangInteger, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __QinJavaUtilList } from "@qin/java-sdk-js";
import { __qin_subhuti_parser_create } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const IllegalArgumentException = __QinJavaLangIllegalArgumentException;
const ArrayList = __QinJavaUtilArrayList;
const Boolean = __QinJavaLangBoolean;
const Double = __QinJavaLangDouble;
const Character = __QinJavaLangCharacter;
const StringBuilder = __QinJavaLangStringBuilder;
const Integer = __QinJavaLangInteger;
class com_slime_java_ast_JavaCstToAst {
  constructor(...__qin_args: any[]) {
    switch (__qin_args.length) {
      case 0: {
        this.__qin_constructor_com_slime_java_ast_JavaCstToAst_0();
        return;
      }
      default: throw new Error("Unsupported Java constructor arity: JavaCstToAst/" + __qin_args.length);
    }
  }
  __qin_constructor_com_slime_java_ast_JavaCstToAst_0(): void {
    null;
  }
  static parse(source: string): any {
    let parser: any = __qin_subhuti_parser_create(__qin_java_class_info__(com_slime_java_JavaParser, { name: "com.slime.java.JavaParser" }), source);
    parser.compilationUnit();
    if ((() => {
      if (com_slime_java_ast_JavaCstToAst.isAtEof(parser)) {
        return false;
      }
      return true;
    })()) {
      throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Java source did not parse to EOF: ", com_slime_java_ast_JavaCstToAst.currentTokenInfo(parser)));
    }
    return com_slime_java_ast_JavaCstToAst.lower(parser.getCst());
  }
  static lower(root: com_subhuti_struct_SubhutiCst): any {
    if ((() => {
      if (__qin_binary__("==", root, null)) {
        return true;
      }
      return (() => {
      if (__QinJavaLangString.equals("compilationUnit", root.getName())) {
        return false;
      }
      return true;
    })();
    })()) {
      throw new __QinJavaLangIllegalArgumentException("Expected compilationUnit CST root");
    }
    let packageName: any = null;
    let packageDeclaration: any = com_slime_java_ast_JavaCstToAst.child(root, "packageDeclaration");
    if (__qin_binary__("!=", packageDeclaration, null)) {
      packageName = com_slime_java_ast_JavaCstToAst.qualifiedName(packageDeclaration);
    }
    let imports: any = new __QinJavaUtilArrayList();
    for (const importDeclaration of com_slime_java_ast_JavaCstToAst.children(root, "importDeclaration")) {
      imports.add(com_slime_java_ast_JavaCstToAst.lowerImport(importDeclaration));
    }
    let classes: any = new __QinJavaUtilArrayList();
    for (const typeDeclaration of com_slime_java_ast_JavaCstToAst.children(root, "typeDeclaration")) {
      let classDeclaration: any = com_slime_java_ast_JavaCstToAst.child(typeDeclaration, "classDeclaration");
      if (__qin_binary__("!=", classDeclaration, null)) {
        classes.add(com_slime_java_ast_JavaCstToAst.lowerClass(classDeclaration, com_slime_java_ast_JavaCstToAst.lowerAnnotations(typeDeclaration)));
      }
      let enumDeclaration: any = com_slime_java_ast_JavaCstToAst.child(typeDeclaration, "enumDeclaration");
      if (__qin_binary__("!=", enumDeclaration, null)) {
        classes.add(com_slime_java_ast_JavaCstToAst.lowerEnum(enumDeclaration, com_slime_java_ast_JavaCstToAst.lowerAnnotations(typeDeclaration)));
      }
      let interfaceDeclaration: any = com_slime_java_ast_JavaCstToAst.child(typeDeclaration, "interfaceDeclaration");
      if (__qin_binary__("!=", interfaceDeclaration, null)) {
        classes.add(com_slime_java_ast_JavaCstToAst.lowerInterface(interfaceDeclaration, com_slime_java_ast_JavaCstToAst.lowerAnnotations(typeDeclaration)));
      }
      let recordDeclaration: any = com_slime_java_ast_JavaCstToAst.child(typeDeclaration, "recordDeclaration");
      if (__qin_binary__("!=", recordDeclaration, null)) {
        classes.add(com_slime_java_ast_JavaCstToAst.lowerRecord(recordDeclaration, com_slime_java_ast_JavaCstToAst.lowerAnnotations(typeDeclaration)));
      }
    }
    return new com_slime_java_ast_JavaAstProgram(packageName, imports, classes);
  }
  static lowerImport(node: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstImportDeclaration(com_slime_java_ast_JavaCstToAst.qualifiedName(node), __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "STATIC"), null), com_slime_java_ast_JavaCstToAst.hasTokenValue(node, "*"));
  }
  static lowerClass(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    let name: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "typeIdentifier"));
    let superTypeName: any = (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(node, "EXTENDS"), null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType"));
    })();
    let implementsTypeNames: any = com_slime_java_ast_JavaCstToAst.lowerImplementsTypeNames(node);
    let typeParameters: any = com_slime_java_ast_JavaCstToAst.lowerTypeParameters(com_slime_java_ast_JavaCstToAst.child(node, "typeParameters"));
    let fields: any = new __QinJavaUtilArrayList();
    let methods: any = new __QinJavaUtilArrayList();
    let staticInitializers: any = new __QinJavaUtilArrayList();
    let nestedClasses: any = new __QinJavaUtilArrayList();
    let classBody: any = com_slime_java_ast_JavaCstToAst.child(node, "classBody");
    if (__qin_binary__("!=", classBody, null)) {
      for (const bodyDeclaration of com_slime_java_ast_JavaCstToAst.children(classBody, "classBodyDeclaration")) {
        let initializerBlock: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "block");
        if ((() => {
      if (__qin_binary__("!=", initializerBlock, null)) {
        return com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static");
      }
      return false;
    })()) {
          staticInitializers.add(new com_slime_java_ast_JavaAstStaticInitializer(com_slime_java_ast_JavaCstToAst.lowerBlock(initializerBlock)));
          continue;
        }
        let member: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "memberDeclaration");
        if (__qin_binary__("==", member, null)) {
          continue;
        }
        let field: any = com_slime_java_ast_JavaCstToAst.child(member, "fieldDeclaration");
        if (__qin_binary__("!=", field, null)) {
          fields.addAll(com_slime_java_ast_JavaCstToAst.lowerFields(field, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static")));
        }
        let method: any = com_slime_java_ast_JavaCstToAst.child(member, "methodDeclaration");
        if (__qin_binary__("!=", method, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(method, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let genericMethod: any = com_slime_java_ast_JavaCstToAst.child(member, "genericMethodDeclaration");
        if (__qin_binary__("!=", genericMethod, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(com_slime_java_ast_JavaCstToAst.child(genericMethod, "methodDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let constructor: any = com_slime_java_ast_JavaCstToAst.child(member, "constructorDeclaration");
        if (__qin_binary__("!=", constructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(constructor, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let genericConstructor: any = com_slime_java_ast_JavaCstToAst.child(member, "genericConstructorDeclaration");
        if (__qin_binary__("!=", genericConstructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(com_slime_java_ast_JavaCstToAst.child(genericConstructor, "constructorDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedClass: any = com_slime_java_ast_JavaCstToAst.child(member, "classDeclaration");
        if (__qin_binary__("!=", nestedClass, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerClass(nestedClass, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedEnum: any = com_slime_java_ast_JavaCstToAst.child(member, "enumDeclaration");
        if (__qin_binary__("!=", nestedEnum, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerEnum(nestedEnum, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedRecord: any = com_slime_java_ast_JavaCstToAst.child(member, "recordDeclaration");
        if (__qin_binary__("!=", nestedRecord, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerRecord(nestedRecord, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
      }
    }
    return new com_slime_java_ast_JavaAstClassDeclaration(name, superTypeName, implementsTypeNames, typeParameters, annotations, fields, methods, staticInitializers, nestedClasses);
  }
  static lowerInterface(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    let name: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "typeIdentifier"));
    let extendsTypeNames: any = (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(node, "EXTENDS"), null)) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerTypeList(com_slime_java_ast_JavaCstToAst.child(node, "typeList"));
    })();
    let methods: any = new __QinJavaUtilArrayList();
    let nestedClasses: any = new __QinJavaUtilArrayList();
    let interfaceBody: any = com_slime_java_ast_JavaCstToAst.child(node, "interfaceBody");
    for (const bodyDeclaration of com_slime_java_ast_JavaCstToAst.children(interfaceBody, "interfaceBodyDeclaration")) {
      let member: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "interfaceMemberDeclaration");
      if (__qin_binary__("==", member, null)) {
        continue;
      }
      let method: any = com_slime_java_ast_JavaCstToAst.child(member, "interfaceMethodDeclaration");
      if (__qin_binary__("!=", method, null)) {
        methods.add(com_slime_java_ast_JavaCstToAst.lowerInterfaceMethod(com_slime_java_ast_JavaCstToAst.child(method, "interfaceCommonBodyDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static")));
      }
      let genericMethod: any = com_slime_java_ast_JavaCstToAst.child(member, "genericInterfaceMethodDeclaration");
      if (__qin_binary__("!=", genericMethod, null)) {
        methods.add(com_slime_java_ast_JavaCstToAst.lowerInterfaceMethod(com_slime_java_ast_JavaCstToAst.child(genericMethod, "interfaceCommonBodyDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static")));
      }
      let nestedClass: any = com_slime_java_ast_JavaCstToAst.child(member, "classDeclaration");
      if (__qin_binary__("!=", nestedClass, null)) {
        nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerClass(nestedClass, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
      }
      let nestedEnum: any = com_slime_java_ast_JavaCstToAst.child(member, "enumDeclaration");
      if (__qin_binary__("!=", nestedEnum, null)) {
        nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerEnum(nestedEnum, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
      }
      let nestedInterface: any = com_slime_java_ast_JavaCstToAst.child(member, "interfaceDeclaration");
      if (__qin_binary__("!=", nestedInterface, null)) {
        nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerInterface(nestedInterface, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
      }
      let nestedRecord: any = com_slime_java_ast_JavaCstToAst.child(member, "recordDeclaration");
      if (__qin_binary__("!=", nestedRecord, null)) {
        nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerRecord(nestedRecord, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
      }
    }
    return new com_slime_java_ast_JavaAstClassDeclaration(name, null, extendsTypeNames, com_slime_java_ast_JavaCstToAst.lowerTypeParameters(com_slime_java_ast_JavaCstToAst.child(node, "typeParameters")), annotations, __QinJavaUtilList.of(), methods, __QinJavaUtilList.of(), nestedClasses);
  }
  static lowerRecord(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    let name: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "typeIdentifier"));
    let implementsTypeNames: any = com_slime_java_ast_JavaCstToAst.lowerImplementsTypeNames(node);
    let fields: any = new __QinJavaUtilArrayList();
    let methods: any = new __QinJavaUtilArrayList();
    let staticInitializers: any = new __QinJavaUtilArrayList();
    let nestedClasses: any = new __QinJavaUtilArrayList();
    let componentList: any = com_slime_java_ast_JavaCstToAst.findFirst(node, "recordComponentList");
    for (const component of com_slime_java_ast_JavaCstToAst.children(componentList, "recordComponent")) {
      fields.add(new com_slime_java_ast_JavaAstFieldDeclaration(com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(component, "typeType")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(component, "identifier")), __QinJavaUtilList.of(), null, true));
    }
    let recordBody: any = com_slime_java_ast_JavaCstToAst.child(node, "recordBody");
    if (__qin_binary__("!=", recordBody, null)) {
      for (const compactConstructor of com_slime_java_ast_JavaCstToAst.children(recordBody, "compactConstructorDeclaration")) {
        methods.add(com_slime_java_ast_JavaCstToAst.lowerCompactConstructor(compactConstructor, com_slime_java_ast_JavaCstToAst.lowerAnnotations(compactConstructor)));
      }
      for (const bodyDeclaration of com_slime_java_ast_JavaCstToAst.children(recordBody, "classBodyDeclaration")) {
        let initializerBlock: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "block");
        if ((() => {
      if (__qin_binary__("!=", initializerBlock, null)) {
        return com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static");
      }
      return false;
    })()) {
          staticInitializers.add(new com_slime_java_ast_JavaAstStaticInitializer(com_slime_java_ast_JavaCstToAst.lowerBlock(initializerBlock)));
          continue;
        }
        let member: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "memberDeclaration");
        if (__qin_binary__("==", member, null)) {
          continue;
        }
        let field: any = com_slime_java_ast_JavaCstToAst.child(member, "fieldDeclaration");
        if (__qin_binary__("!=", field, null)) {
          fields.addAll(com_slime_java_ast_JavaCstToAst.lowerFields(field, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static")));
        }
        let method: any = com_slime_java_ast_JavaCstToAst.child(member, "methodDeclaration");
        if (__qin_binary__("!=", method, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(method, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let genericMethod: any = com_slime_java_ast_JavaCstToAst.child(member, "genericMethodDeclaration");
        if (__qin_binary__("!=", genericMethod, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(com_slime_java_ast_JavaCstToAst.child(genericMethod, "methodDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let constructor: any = com_slime_java_ast_JavaCstToAst.child(member, "constructorDeclaration");
        if (__qin_binary__("!=", constructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(constructor, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let genericConstructor: any = com_slime_java_ast_JavaCstToAst.child(member, "genericConstructorDeclaration");
        if (__qin_binary__("!=", genericConstructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(com_slime_java_ast_JavaCstToAst.child(genericConstructor, "constructorDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedClass: any = com_slime_java_ast_JavaCstToAst.child(member, "classDeclaration");
        if (__qin_binary__("!=", nestedClass, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerClass(nestedClass, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedEnum: any = com_slime_java_ast_JavaCstToAst.child(member, "enumDeclaration");
        if (__qin_binary__("!=", nestedEnum, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerEnum(nestedEnum, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedRecord: any = com_slime_java_ast_JavaCstToAst.child(member, "recordDeclaration");
        if (__qin_binary__("!=", nestedRecord, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerRecord(nestedRecord, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
      }
    }
    return new com_slime_java_ast_JavaAstClassDeclaration(name, null, implementsTypeNames, __QinJavaUtilList.of(), annotations, fields, methods, staticInitializers, nestedClasses, true);
  }
  static lowerCompactConstructor(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    return new com_slime_java_ast_JavaAstMethodDeclaration("void", "constructor", annotations, __QinJavaUtilList.of(), com_slime_java_ast_JavaCstToAst.lowerBlock(com_slime_java_ast_JavaCstToAst.child(node, "block")), null);
  }
  static lowerEnum(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    let name: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "typeIdentifier"));
    let fields: any = new __QinJavaUtilArrayList();
    let enumConstants: any = com_slime_java_ast_JavaCstToAst.child(node, "enumConstants");
    if (__qin_binary__("!=", enumConstants, null)) {
      for (const enumConstant of com_slime_java_ast_JavaCstToAst.children(enumConstants, "enumConstant")) {
        let constantName: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(enumConstant, "identifier"));
        fields.add(new com_slime_java_ast_JavaAstFieldDeclaration(name, constantName, __QinJavaUtilList.of(), new com_slime_java_ast_JavaAstNewExpression(name, com_slime_java_ast_JavaCstToAst.lowerArguments(com_slime_java_ast_JavaCstToAst.child(enumConstant, "arguments")))));
      }
    }
    let methods: any = new __QinJavaUtilArrayList();
    let staticInitializers: any = new __QinJavaUtilArrayList();
    let nestedClasses: any = new __QinJavaUtilArrayList();
    let enumBodyDeclarations: any = com_slime_java_ast_JavaCstToAst.child(node, "enumBodyDeclarations");
    if (__qin_binary__("!=", enumBodyDeclarations, null)) {
      for (const bodyDeclaration of com_slime_java_ast_JavaCstToAst.children(enumBodyDeclarations, "classBodyDeclaration")) {
        let initializerBlock: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "block");
        if ((() => {
      if (__qin_binary__("!=", initializerBlock, null)) {
        return com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static");
      }
      return false;
    })()) {
          staticInitializers.add(new com_slime_java_ast_JavaAstStaticInitializer(com_slime_java_ast_JavaCstToAst.lowerBlock(initializerBlock)));
          continue;
        }
        let member: any = com_slime_java_ast_JavaCstToAst.child(bodyDeclaration, "memberDeclaration");
        if (__qin_binary__("==", member, null)) {
          continue;
        }
        let field: any = com_slime_java_ast_JavaCstToAst.child(member, "fieldDeclaration");
        if (__qin_binary__("!=", field, null)) {
          fields.addAll(com_slime_java_ast_JavaCstToAst.lowerFields(field, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static")));
        }
        let method: any = com_slime_java_ast_JavaCstToAst.child(member, "methodDeclaration");
        if (__qin_binary__("!=", method, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(method, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let genericMethod: any = com_slime_java_ast_JavaCstToAst.child(member, "genericMethodDeclaration");
        if (__qin_binary__("!=", genericMethod, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerMethod(com_slime_java_ast_JavaCstToAst.child(genericMethod, "methodDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "static"), com_slime_java_ast_JavaCstToAst.hasTokenValue(bodyDeclaration, "abstract")));
        }
        let constructor: any = com_slime_java_ast_JavaCstToAst.child(member, "constructorDeclaration");
        if (__qin_binary__("!=", constructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(constructor, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let genericConstructor: any = com_slime_java_ast_JavaCstToAst.child(member, "genericConstructorDeclaration");
        if (__qin_binary__("!=", genericConstructor, null)) {
          methods.add(com_slime_java_ast_JavaCstToAst.lowerConstructor(com_slime_java_ast_JavaCstToAst.child(genericConstructor, "constructorDeclaration"), com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedClass: any = com_slime_java_ast_JavaCstToAst.child(member, "classDeclaration");
        if (__qin_binary__("!=", nestedClass, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerClass(nestedClass, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedEnum: any = com_slime_java_ast_JavaCstToAst.child(member, "enumDeclaration");
        if (__qin_binary__("!=", nestedEnum, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerEnum(nestedEnum, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
        let nestedRecord: any = com_slime_java_ast_JavaCstToAst.child(member, "recordDeclaration");
        if (__qin_binary__("!=", nestedRecord, null)) {
          nestedClasses.add(com_slime_java_ast_JavaCstToAst.lowerRecord(nestedRecord, com_slime_java_ast_JavaCstToAst.lowerAnnotations(bodyDeclaration)));
        }
      }
    }
    return new com_slime_java_ast_JavaAstClassDeclaration(name, "Enum", __QinJavaUtilList.of(), __QinJavaUtilList.of(), annotations, fields, methods, staticInitializers, nestedClasses);
  }
  static lowerImplementsTypeNames(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(node, "IMPLEMENTS"), null)) {
      return __QinJavaUtilList.of();
    }
    let typeList: any = com_slime_java_ast_JavaCstToAst.child(node, "typeList");
    if (__qin_binary__("==", typeList, null)) {
      return __QinJavaUtilList.of();
    }
    return com_slime_java_ast_JavaCstToAst.lowerTypeList(typeList);
  }
  static lowerTypeList(typeList: com_subhuti_struct_SubhutiCst): any {
    let types: any = new __QinJavaUtilArrayList();
    for (const typeType of com_slime_java_ast_JavaCstToAst.children(typeList, "typeType")) {
      types.add(com_slime_java_ast_JavaCstToAst.typeName(typeType));
    }
    return types;
  }
  static lowerNonWildcardTypeArguments(nonWildcardTypeArguments: com_subhuti_struct_SubhutiCst): any {
    return com_slime_java_ast_JavaCstToAst.lowerTypeList(com_slime_java_ast_JavaCstToAst.child(nonWildcardTypeArguments, "typeList"));
  }
  static lowerTypeParameters(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", node, null)) {
      return __QinJavaUtilList.of();
    }
    let parameters: any = new __QinJavaUtilArrayList();
    for (const typeParameter of com_slime_java_ast_JavaCstToAst.children(node, "typeParameter")) {
      let name: any = com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(typeParameter, "typeIdentifier"));
      let typeBound: any = com_slime_java_ast_JavaCstToAst.child(typeParameter, "typeBound");
      let boundTypeName: any = (() => {
      if (__qin_binary__("==", typeBound, null)) {
        return "Object";
      }
      return com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(typeBound, "typeType"));
    })();
      parameters.add(new com_slime_java_ast_JavaAstTypeParameter(name, boundTypeName));
    }
    return parameters;
  }
  static lowerFields(node: com_subhuti_struct_SubhutiCst, annotations: any, staticField: boolean): any {
    let typeName: any = com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType"));
    let fields: any = new __QinJavaUtilArrayList();
    let declarators: any = com_slime_java_ast_JavaCstToAst.child(node, "variableDeclarators");
    for (const declarator of com_slime_java_ast_JavaCstToAst.children(declarators, "variableDeclarator")) {
      let declaratorId: any = com_slime_java_ast_JavaCstToAst.child(declarator, "variableDeclaratorId");
      let initializer: any = com_slime_java_ast_JavaCstToAst.child(declarator, "variableInitializer");
      fields.add(new com_slime_java_ast_JavaAstFieldDeclaration(typeName, com_slime_java_ast_JavaCstToAst.identifierValue(declaratorId), annotations, (() => {
      if (__qin_binary__("==", initializer, null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.lowerVariableInitializer(initializer);
    })(), false, staticField));
    }
    return fields;
  }
  static lowerMethod(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_struct_SubhutiCst)) && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_args[1] instanceof __QinJavaUtilArrayList || __qin_args[1] instanceof __QinJavaUtilUnmodifiableList)) return this.__qin_overload_lowerMethod_2_0(__qin_args[0], (Array.isArray(__qin_args[1]) ? new __QinJavaUtilArrayList(__qin_args[1]) : __qin_args[1]));
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_struct_SubhutiCst)) && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_args[1] instanceof __QinJavaUtilArrayList || __qin_args[1] instanceof __QinJavaUtilUnmodifiableList) && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean") return this.__qin_overload_lowerMethod_4_1(__qin_args[0], (Array.isArray(__qin_args[1]) ? new __QinJavaUtilArrayList(__qin_args[1]) : __qin_args[1]), __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: lowerMethod/" + __qin_args.length);
  }
  static __qin_overload_lowerMethod_2_0(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    return com_slime_java_ast_JavaCstToAst.lowerMethod(node, annotations, false, false);
  }
  static __qin_overload_lowerMethod_4_1(node: com_subhuti_struct_SubhutiCst, annotations: any, staticMethod: boolean, abstractMethod: boolean): any {
    return new com_slime_java_ast_JavaAstMethodDeclaration(com_slime_java_ast_JavaCstToAst.typeNameOrVoid(com_slime_java_ast_JavaCstToAst.child(node, "typeTypeOrVoid")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "identifier")), annotations, com_slime_java_ast_JavaCstToAst.lowerParameters(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "formalParameters"), "formalParameterList")), com_slime_java_ast_JavaCstToAst.lowerMethodBodyStatements(node), com_slime_java_ast_JavaCstToAst.lastReturnExpression(com_slime_java_ast_JavaCstToAst.lowerMethodBodyStatements(node)), staticMethod, abstractMethod);
  }
  static lowerInterfaceMethod(node: com_subhuti_struct_SubhutiCst, declarationAnnotations: any, staticMethod: boolean): any {
    let annotations: any = new __QinJavaUtilArrayList(declarationAnnotations);
    annotations.addAll(com_slime_java_ast_JavaCstToAst.lowerAnnotations(node));
    let bodyStatements: any = com_slime_java_ast_JavaCstToAst.lowerMethodBodyStatements(node);
    let abstractMethod: any = __qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "methodBody"), "block"), null);
    return new com_slime_java_ast_JavaAstMethodDeclaration(com_slime_java_ast_JavaCstToAst.typeNameOrVoid(com_slime_java_ast_JavaCstToAst.child(node, "typeTypeOrVoid")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "identifier")), annotations, com_slime_java_ast_JavaCstToAst.lowerParameters(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "formalParameters"), "formalParameterList")), bodyStatements, com_slime_java_ast_JavaCstToAst.lastReturnExpression(bodyStatements), staticMethod, abstractMethod);
  }
  static lowerConstructor(node: com_subhuti_struct_SubhutiCst, annotations: any): any {
    return new com_slime_java_ast_JavaAstMethodDeclaration("void", "constructor", annotations, com_slime_java_ast_JavaCstToAst.lowerParameters(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "formalParameters"), "formalParameterList")), com_slime_java_ast_JavaCstToAst.lowerConstructorBodyStatements(node), null);
  }
  static lowerParameters(parameterList: com_subhuti_struct_SubhutiCst): any {
    let parameters: any = new __QinJavaUtilArrayList();
    for (const parameter of com_slime_java_ast_JavaCstToAst.children(parameterList, "formalParameter")) {
      parameters.add(new com_slime_java_ast_JavaAstParameter(com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(parameter, "typeType")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(parameter, "variableDeclaratorId"))));
    }
    for (const parameter of com_slime_java_ast_JavaCstToAst.children(parameterList, "lastFormalParameter")) {
      parameters.add(new com_slime_java_ast_JavaAstParameter(__qin_binary__("+", com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(parameter, "typeType")), "[]"), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(parameter, "variableDeclaratorId")), true));
    }
    return parameters;
  }
  static lowerConstructorBodyStatements(constructorDeclaration: com_subhuti_struct_SubhutiCst): any {
    let block: any = com_slime_java_ast_JavaCstToAst.child(constructorDeclaration, "block");
    if (__qin_binary__("==", block, null)) {
      return __QinJavaUtilList.of();
    }
    let statements: any = new __QinJavaUtilArrayList();
    let explicitConstructorInvocation: any = com_slime_java_ast_JavaCstToAst.explicitConstructorInvocation(block);
    if (__qin_binary__("!=", explicitConstructorInvocation, null)) {
      statements.add(com_slime_java_ast_JavaCstToAst.lowerExplicitConstructorInvocationStatement(explicitConstructorInvocation));
    }
    statements.addAll(com_slime_java_ast_JavaCstToAst.lowerBlock(block));
    return statements;
  }
  static lowerExplicitConstructorInvocationStatement(statement: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstExpressionStatement(new com_slime_java_ast_JavaAstMethodCallExpression((() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(statement, "THIS"), null)) {
        return new com_slime_java_ast_JavaAstIdentifierExpression("super");
      }
      return new com_slime_java_ast_JavaAstThisExpression();
    })(), "constructor", com_slime_java_ast_JavaCstToAst.lowerArguments(com_slime_java_ast_JavaCstToAst.child(statement, "arguments"))));
  }
  static explicitConstructorInvocation(block: com_subhuti_struct_SubhutiCst): any {
    for (const blockStatement of com_slime_java_ast_JavaCstToAst.children(block, "blockStatement")) {
      let statement: any = com_slime_java_ast_JavaCstToAst.child(blockStatement, "statement");
      let explicitConstructorInvocation: any = com_slime_java_ast_JavaCstToAst.child(statement, "explicitConstructorInvocationStatement");
      if (__qin_binary__("!=", explicitConstructorInvocation, null)) {
        return explicitConstructorInvocation;
      }
    }
    return null;
  }
  static lowerMethodBodyStatements(methodDeclaration: com_subhuti_struct_SubhutiCst): any {
    let methodBody: any = com_slime_java_ast_JavaCstToAst.child(methodDeclaration, "methodBody");
    let block: any = com_slime_java_ast_JavaCstToAst.child(methodBody, "block");
    if (__qin_binary__("==", block, null)) {
      return __QinJavaUtilList.of();
    }
    let statements: any = new __QinJavaUtilArrayList();
    for (const blockStatement of com_slime_java_ast_JavaCstToAst.children(block, "blockStatement")) {
      statements.addAll(com_slime_java_ast_JavaCstToAst.lowerBlockStatement(blockStatement));
    }
    return statements;
  }
  static lowerBlockStatement(blockStatement: com_subhuti_struct_SubhutiCst): any {
    let localVariableDeclaration: any = com_slime_java_ast_JavaCstToAst.child(blockStatement, "localVariableDeclaration");
    if (__qin_binary__("!=", localVariableDeclaration, null)) {
      return com_slime_java_ast_JavaCstToAst.lowerLocalVariableDeclarations(localVariableDeclaration);
    }
    let statement: any = com_slime_java_ast_JavaCstToAst.child(blockStatement, "statement");
    if (__qin_binary__("!=", statement, null)) {
      return com_slime_java_ast_JavaCstToAst.lowerStatement(statement);
    }
    return __QinJavaUtilList.of();
  }
  static lowerStatement(statement: com_subhuti_struct_SubhutiCst): any {
    let block: any = com_slime_java_ast_JavaCstToAst.child(statement, "block");
    if (__qin_binary__("!=", block, null)) {
      return com_slime_java_ast_JavaCstToAst.lowerBlock(block);
    }
    let ifStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "ifStatement");
    if (__qin_binary__("!=", ifStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerIfStatement(ifStatement));
    }
    let forStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "forStatement");
    if (__qin_binary__("!=", forStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerForStatement(forStatement));
    }
    let whileStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "whileStatement");
    if (__qin_binary__("!=", whileStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerWhileStatement(whileStatement));
    }
    let doWhileStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "doWhileStatement");
    if (__qin_binary__("!=", doWhileStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerDoWhileStatement(doWhileStatement));
    }
    let tryStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "tryStatement");
    if (__qin_binary__("!=", tryStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerTryStatement(tryStatement));
    }
    let switchStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "switchStatement");
    if (__qin_binary__("!=", switchStatement, null)) {
      return __QinJavaUtilList.of(com_slime_java_ast_JavaCstToAst.lowerSwitchStatement(switchStatement));
    }
    let returnStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "returnStatement");
    if (__qin_binary__("!=", returnStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstReturnStatement(com_slime_java_ast_JavaCstToAst.lowerReturnExpression(returnStatement)));
    }
    let throwStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "throwStatement");
    if (__qin_binary__("!=", throwStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstThrowStatement(com_slime_java_ast_JavaCstToAst.lowerThrowExpression(throwStatement)));
    }
    let yieldStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "yieldStatement");
    if (__qin_binary__("!=", yieldStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstYieldStatement(com_slime_java_ast_JavaCstToAst.lowerYieldExpression(yieldStatement)));
    }
    let breakStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "breakStatement");
    if (__qin_binary__("!=", breakStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstBreakStatement(com_slime_java_ast_JavaCstToAst.optionalIdentifierValue(breakStatement)));
    }
    let continueStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "continueStatement");
    if (__qin_binary__("!=", continueStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstContinueStatement(com_slime_java_ast_JavaCstToAst.optionalIdentifierValue(continueStatement)));
    }
    let expressionStatement: any = com_slime_java_ast_JavaCstToAst.child(statement, "expressionStatement");
    if (__qin_binary__("!=", expressionStatement, null)) {
      let statementExpression: any = com_slime_java_ast_JavaCstToAst.child(expressionStatement, "statementExpression");
      let expression: any = com_slime_java_ast_JavaCstToAst.child(statementExpression, "expression");
      if (__qin_binary__("!=", expression, null)) {
        return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstExpressionStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(expression)));
      }
    }
    return __QinJavaUtilList.of();
  }
  static lowerBlock(block: com_subhuti_struct_SubhutiCst): any {
    let statements: any = new __QinJavaUtilArrayList();
    for (const blockStatement of com_slime_java_ast_JavaCstToAst.children(block, "blockStatement")) {
      statements.addAll(com_slime_java_ast_JavaCstToAst.lowerBlockStatement(blockStatement));
    }
    return statements;
  }
  static lowerIfStatement(ifStatement: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstIfStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(ifStatement, "parExpression"), "expression")), (() => {
      if (com_slime_java_ast_JavaCstToAst.children(ifStatement, "statement").isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(com_slime_java_ast_JavaCstToAst.children(ifStatement, "statement").get(0.0));
    })(), (() => {
      if (__qin_binary__("<", com_slime_java_ast_JavaCstToAst.children(ifStatement, "statement").size(), 2.0)) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(com_slime_java_ast_JavaCstToAst.children(ifStatement, "statement").get(1.0));
    })());
  }
  static lowerWhileStatement(whileStatement: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstWhileStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(whileStatement, "parExpression"), "expression")), (() => {
      if (com_slime_java_ast_JavaCstToAst.children(whileStatement, "statement").isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(com_slime_java_ast_JavaCstToAst.children(whileStatement, "statement").get(0.0));
    })());
  }
  static lowerDoWhileStatement(doWhileStatement: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstDoWhileStatement((() => {
      if (com_slime_java_ast_JavaCstToAst.children(doWhileStatement, "statement").isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(com_slime_java_ast_JavaCstToAst.children(doWhileStatement, "statement").get(0.0));
    })(), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(doWhileStatement, "parExpression"), "expression")));
  }
  static lowerTryStatement(tryStatement: com_subhuti_struct_SubhutiCst): any {
    let blocks: any = com_slime_java_ast_JavaCstToAst.children(tryStatement, "block");
    let tryStatements: any = (() => {
      if (blocks.isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerBlock(blocks.get(0.0));
    })();
    let catchClauses: any = new __QinJavaUtilArrayList();
    for (const catchClause of com_slime_java_ast_JavaCstToAst.children(tryStatement, "catchClause")) {
      catchClauses.add(com_slime_java_ast_JavaCstToAst.lowerCatchClause(catchClause));
    }
    let finallyBlock: any = com_slime_java_ast_JavaCstToAst.child(tryStatement, "finallyBlock");
    let finallyStatements: any = (() => {
      if (__qin_binary__("==", finallyBlock, null)) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerBlock(com_slime_java_ast_JavaCstToAst.child(finallyBlock, "block"));
    })();
    return new com_slime_java_ast_JavaAstTryStatement(tryStatements, catchClauses, finallyStatements);
  }
  static lowerSwitchStatement(switchStatement: com_subhuti_struct_SubhutiCst): any {
    let parExpression: any = com_slime_java_ast_JavaCstToAst.child(switchStatement, "parExpression");
    let cases: any = new __QinJavaUtilArrayList();
    for (const group of com_slime_java_ast_JavaCstToAst.children(switchStatement, "switchBlockStatementGroup")) {
      cases.addAll(com_slime_java_ast_JavaCstToAst.lowerSwitchBlockStatementGroup(group));
    }
    for (const rule of com_slime_java_ast_JavaCstToAst.children(switchStatement, "switchRule")) {
      cases.addAll(com_slime_java_ast_JavaCstToAst.lowerSwitchRule(rule));
    }
    for (const label of com_slime_java_ast_JavaCstToAst.children(switchStatement, "switchLabel")) {
      cases.add(new com_slime_java_ast_JavaAstSwitchCase(com_slime_java_ast_JavaCstToAst.lowerSwitchLabelExpression(label), __QinJavaUtilList.of()));
    }
    return new com_slime_java_ast_JavaAstSwitchStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(parExpression, "expression")), cases);
  }
  static lowerSwitchExpression(switchExpression: com_subhuti_struct_SubhutiCst): any {
    let parExpression: any = com_slime_java_ast_JavaCstToAst.child(switchExpression, "parExpression");
    let cases: any = new __QinJavaUtilArrayList();
    for (const rule of com_slime_java_ast_JavaCstToAst.children(switchExpression, "switchRule")) {
      cases.addAll(com_slime_java_ast_JavaCstToAst.lowerSwitchRule(rule));
    }
    return new com_slime_java_ast_JavaAstSwitchExpression(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(parExpression, "expression")), cases);
  }
  static lowerSwitchBlockStatementGroup(group: com_subhuti_struct_SubhutiCst): any {
    let labels: any = com_slime_java_ast_JavaCstToAst.children(group, "switchLabel");
    let statements: any = new __QinJavaUtilArrayList();
    for (const blockStatement of com_slime_java_ast_JavaCstToAst.children(group, "blockStatement")) {
      statements.addAll(com_slime_java_ast_JavaCstToAst.lowerBlockStatement(blockStatement));
    }
    if (labels.isEmpty()) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstSwitchCase(null, statements));
    }
    let cases: any = new __QinJavaUtilArrayList();
    for (let i: any = 0.0; __qin_binary__("<", i, labels.size()); i++) {
      cases.add(new com_slime_java_ast_JavaAstSwitchCase(com_slime_java_ast_JavaCstToAst.lowerSwitchLabelExpression(labels.get(i)), (() => {
      if (__qin_binary__("==", i, __qin_binary__("-", labels.size(), 1.0))) {
        return statements;
      }
      return __QinJavaUtilList.of();
    })()));
    }
    return cases;
  }
  static lowerSwitchRule(rule: com_subhuti_struct_SubhutiCst): any {
    let statements: any = com_slime_java_ast_JavaCstToAst.lowerSwitchRuleOutcome(com_slime_java_ast_JavaCstToAst.child(rule, "switchRuleOutcome"));
    let expressionList: any = com_slime_java_ast_JavaCstToAst.child(rule, "expressionList");
    let switchRuleLabelList: any = com_slime_java_ast_JavaCstToAst.child(rule, "switchRuleLabelList");
    if ((() => {
      if (__qin_binary__("==", expressionList, null)) {
        return __qin_binary__("==", switchRuleLabelList, null);
      }
      return false;
    })()) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstSwitchCase(null, statements));
    }
    let tests: any = (() => {
      if (__qin_binary__("!=", expressionList, null)) {
        return com_slime_java_ast_JavaCstToAst.lowerExpressionList(expressionList);
      }
      return com_slime_java_ast_JavaCstToAst.lowerSwitchRuleLabelList(switchRuleLabelList);
    })();
    if (tests.isEmpty()) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstSwitchCase(null, statements));
    }
    let cases: any = new __QinJavaUtilArrayList();
    for (let i: any = 0.0; __qin_binary__("<", i, tests.size()); i++) {
      cases.add(new com_slime_java_ast_JavaAstSwitchCase(tests.get(i), (() => {
      if (__qin_binary__("==", i, __qin_binary__("-", tests.size(), 1.0))) {
        return statements;
      }
      return __QinJavaUtilList.of();
    })()));
    }
    return cases;
  }
  static lowerSwitchRuleLabelList(switchRuleLabelList: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", switchRuleLabelList, null)) {
      return __QinJavaUtilList.of();
    }
    let lowered: any = new __QinJavaUtilArrayList();
    for (const label of com_slime_java_ast_JavaCstToAst.children(switchRuleLabelList, "assignmentExpression")) {
      lowered.add(com_slime_java_ast_JavaCstToAst.lowerExpression(label));
    }
    return lowered;
  }
  static lowerSwitchRuleOutcome(outcome: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", outcome, null)) {
      return __QinJavaUtilList.of();
    }
    let block: any = com_slime_java_ast_JavaCstToAst.child(outcome, "block");
    if (__qin_binary__("!=", block, null)) {
      return com_slime_java_ast_JavaCstToAst.lowerBlock(block);
    }
    let throwStatement: any = com_slime_java_ast_JavaCstToAst.child(outcome, "throwStatement");
    if (__qin_binary__("!=", throwStatement, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstThrowStatement(com_slime_java_ast_JavaCstToAst.lowerThrowExpression(throwStatement)));
    }
    let expressionStatement: any = com_slime_java_ast_JavaCstToAst.child(outcome, "expressionStatement");
    if (__qin_binary__("!=", expressionStatement, null)) {
      let statementExpression: any = com_slime_java_ast_JavaCstToAst.child(expressionStatement, "statementExpression");
      let expression: any = com_slime_java_ast_JavaCstToAst.child(statementExpression, "expression");
      return (() => {
      if (__qin_binary__("==", expression, null)) {
        return __QinJavaUtilList.of();
      }
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstExpressionStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(expression)));
    })();
    }
    let expression: any = com_slime_java_ast_JavaCstToAst.child(outcome, "expression");
    if (__qin_binary__("!=", expression, null)) {
      return __QinJavaUtilList.of(new com_slime_java_ast_JavaAstExpressionStatement(com_slime_java_ast_JavaCstToAst.lowerExpression(expression)));
    }
    return __QinJavaUtilList.of();
  }
  static lowerSwitchLabelExpression(label: com_subhuti_struct_SubhutiCst): any {
    if ((() => {
      if (__qin_binary__("==", label, null)) {
        return true;
      }
      return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(label, "DEFAULT"), null);
    })()) {
      return null;
    }
    return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(label, "expression"));
  }
  static lowerCatchClause(catchClause: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstCatchClause(com_slime_java_ast_JavaCstToAst.qualifiedName(com_slime_java_ast_JavaCstToAst.child(catchClause, "catchType")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(catchClause, "identifier")), com_slime_java_ast_JavaCstToAst.lowerBlock(com_slime_java_ast_JavaCstToAst.child(catchClause, "block")));
  }
  static lowerForStatement(forStatement: com_subhuti_struct_SubhutiCst): any {
    let forControl: any = com_slime_java_ast_JavaCstToAst.child(forStatement, "forControl");
    let enhancedForControl: any = com_slime_java_ast_JavaCstToAst.child(forControl, "enhancedForControl");
    if (__qin_binary__("!=", enhancedForControl, null)) {
      let bodyStatements: any = com_slime_java_ast_JavaCstToAst.children(forStatement, "statement");
      let typeName: any = (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(enhancedForControl, "VAR"), null)) {
        return com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(enhancedForControl, "typeType"));
      }
      return "var";
    })();
      return new com_slime_java_ast_JavaAstEnhancedForStatement(typeName, com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(enhancedForControl, "variableDeclaratorId")), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(enhancedForControl, "expression")), (() => {
      if (bodyStatements.isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(bodyStatements.get(0.0));
    })());
    }
    let forInit: any = com_slime_java_ast_JavaCstToAst.child(forControl, "forInit");
    let initializerStatements: any = __QinJavaUtilList.of();
    if (__qin_binary__("!=", forInit, null)) {
      let localVariableDeclaration: any = com_slime_java_ast_JavaCstToAst.child(forInit, "localVariableDeclaration");
      if (__qin_binary__("!=", localVariableDeclaration, null)) {
        initializerStatements = com_slime_java_ast_JavaCstToAst.lowerLocalVariableDeclarations(localVariableDeclaration);
      } else {
        initializerStatements = com_slime_java_ast_JavaCstToAst.lowerExpressionList(com_slime_java_ast_JavaCstToAst.child(forInit, "expressionList")).stream().map(new com_slime_java_ast_JavaAstExpressionStatement()).toList();
      }
    }
    let test: any = com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(forControl, "expression"));
    let forUpdate: any = com_slime_java_ast_JavaCstToAst.child(forControl, "forUpdate");
    let updateExpressions: any = (() => {
      if (__qin_binary__("==", forUpdate, null)) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerExpressionList(com_slime_java_ast_JavaCstToAst.child(forUpdate, "expressionList"));
    })();
    let bodyStatements: any = com_slime_java_ast_JavaCstToAst.children(forStatement, "statement");
    return new com_slime_java_ast_JavaAstForStatement(initializerStatements, test, updateExpressions, (() => {
      if (bodyStatements.isEmpty()) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerStatement(bodyStatements.get(0.0));
    })());
  }
  static lowerLocalVariableDeclarations(localVariableDeclaration: com_subhuti_struct_SubhutiCst): any {
    let typeName: any = (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(localVariableDeclaration, "VAR"), null)) {
        return com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(localVariableDeclaration, "typeType"));
      }
      return "var";
    })();
    let statements: any = new __QinJavaUtilArrayList();
    let declarators: any = com_slime_java_ast_JavaCstToAst.child(localVariableDeclaration, "variableDeclarators");
    if (__qin_binary__("==", declarators, null)) {
      let identifier: any = com_slime_java_ast_JavaCstToAst.child(localVariableDeclaration, "identifier");
      let expression: any = com_slime_java_ast_JavaCstToAst.child(localVariableDeclaration, "expression");
      statements.add(new com_slime_java_ast_JavaAstLocalVariableDeclaration(typeName, com_slime_java_ast_JavaCstToAst.identifierValue(identifier), com_slime_java_ast_JavaCstToAst.lowerExpression(expression)));
      return statements;
    }
    for (const declarator of com_slime_java_ast_JavaCstToAst.children(declarators, "variableDeclarator")) {
      let initializer: any = com_slime_java_ast_JavaCstToAst.child(declarator, "variableInitializer");
      statements.add(new com_slime_java_ast_JavaAstLocalVariableDeclaration(typeName, com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(declarator, "variableDeclaratorId")), (() => {
      if (__qin_binary__("==", initializer, null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.lowerVariableInitializer(initializer);
    })()));
    }
    return statements;
  }
  static lowerVariableInitializer(initializer: com_subhuti_struct_SubhutiCst): any {
    let arrayInitializer: any = com_slime_java_ast_JavaCstToAst.child(initializer, "arrayInitializer");
    if (__qin_binary__("!=", arrayInitializer, null)) {
      return new com_slime_java_ast_JavaAstArrayLiteralExpression("Object[]", com_slime_java_ast_JavaCstToAst.lowerArrayInitializer(arrayInitializer));
    }
    return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(initializer, "expression"));
  }
  static lowerReturnExpression(returnStatement: com_subhuti_struct_SubhutiCst): any {
    return (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(returnStatement, "expression"), null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(returnStatement, "expression"));
    })();
  }
  static optionalIdentifierValue(statement: com_subhuti_struct_SubhutiCst): any {
    return (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(statement, "identifier"), null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(statement, "identifier"));
    })();
  }
  static lowerThrowExpression(throwStatement: com_subhuti_struct_SubhutiCst): any {
    return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(throwStatement, "expression"));
  }
  static lowerYieldExpression(yieldStatement: com_subhuti_struct_SubhutiCst): any {
    return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(yieldStatement, "expression"));
  }
  static lastReturnExpression(statements: any): any {
    let returnStatement: any = null;
    for (let i: any = __qin_binary__("-", statements.size(), 1.0); __qin_binary__(">=", i, 0.0); i--) {
      if ((() => { const __qin_pattern_value = statements.get(i); return __qin_instanceof__(__qin_pattern_value, com_slime_java_ast_JavaAstReturnStatement) && (returnStatement = __qin_pattern_value, true); })()) {
        return returnStatement.expression();
      }
    }
    return null;
  }
  static lowerExpression(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", node, null)) {
      return null;
    }
    if (node.isToken()) {
      return (() => {
      switch (node.getName()) {
        case "IDENTIFIER":
          return new com_slime_java_ast_JavaAstIdentifierExpression(node.getValue());
        case "DECIMAL_LITERAL":
        case "HEX_LITERAL":
        case "OCT_LITERAL":
        case "BINARY_LITERAL":
        case "FLOAT_LITERAL":
        case "HEX_FLOAT_LITERAL":
          return new com_slime_java_ast_JavaAstNumberLiteral(com_slime_java_ast_JavaCstToAst.numberValue(node.getValue()), com_slime_java_ast_JavaCstToAst.isIntegralNumberToken(node.getName()));
        case "CHAR_LITERAL":
          return new com_slime_java_ast_JavaAstStringLiteral(com_slime_java_ast_JavaCstToAst.charLiteralText(node.getValue()));
        case "BOOL_LITERAL":
          return new com_slime_java_ast_JavaAstBooleanLiteral(__QinJavaLangBoolean.parseBoolean(node.getValue()));
        case "STRING_LITERAL":
          return new com_slime_java_ast_JavaAstStringLiteral(com_slime_java_ast_JavaCstToAst.unquoteString(node.getValue()));
        case "NULL_LITERAL":
          return new com_slime_java_ast_JavaAstNullLiteral();
        case "THIS":
          return new com_slime_java_ast_JavaAstThisExpression();
        case "SUPER":
          return new com_slime_java_ast_JavaAstIdentifierExpression("super");
        default:
          throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Unsupported expression token: ", node.getName()));
      }
      return null;
    })();
    }
    if (__QinJavaLangString.equals("additiveExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLeftAssociative(node, __QinJavaUtilList.of("ADD", "SUB"));
    }
    if (__QinJavaLangString.equals("multiplicativeExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLeftAssociative(node, __QinJavaUtilList.of("MUL", "DIV", "MOD"));
    }
    if (__QinJavaLangString.equals("relationalExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerRelationalExpression(node);
    }
    if (__QinJavaLangString.equals("equalityExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLeftAssociative(node, __QinJavaUtilList.of("EQUAL", "NOTEQUAL"));
    }
    if (__QinJavaLangString.equals("lambdaExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLambdaExpression(node);
    }
    if (__QinJavaLangString.equals("switchExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerSwitchExpression(node);
    }
    if (__QinJavaLangString.equals("conditionalAndExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLeftAssociative(node, __QinJavaUtilList.of("AND"));
    }
    if (__QinJavaLangString.equals("conditionalOrExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerLeftAssociative(node, __QinJavaUtilList.of("OR"));
    }
    if ((() => {
      if (__QinJavaLangString.equals("conditionalExpression", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "QUESTION"), null);
      }
      return false;
    })()) {
      return new com_slime_java_ast_JavaAstConditionalExpression(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(node, "conditionalOrExpression")), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(node, "expression")), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.lastChild(node, "conditionalExpression")));
    }
    if (__QinJavaLangString.equals("assignment", node.getName())) {
      return new com_slime_java_ast_JavaAstAssignmentExpression(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(node, "leftHandSide")), com_slime_java_ast_JavaCstToAst.assignmentOperatorValue(com_slime_java_ast_JavaCstToAst.child(node, "assignmentOperator")), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(node, "expression")));
    }
    if ((() => {
      if (__QinJavaLangString.equals("unaryExpression", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "prefixOp"), null);
      }
      return false;
    })()) {
      return new com_slime_java_ast_JavaAstUnaryExpression(com_slime_java_ast_JavaCstToAst.prefixOperatorValue(com_slime_java_ast_JavaCstToAst.child(node, "prefixOp")), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.lastChild(node, "unaryExpression")));
    }
    if ((() => {
      if (__QinJavaLangString.equals("unaryExpression", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "LPAREN"), null);
      }
      return false;
    })()) {
      let castType: any = com_slime_java_ast_JavaCstToAst.child(node, "typeType");
      if (__qin_binary__("==", castType, null)) {
        castType = com_slime_java_ast_JavaCstToAst.child(node, "primitiveType");
      }
      if (__qin_binary__("!=", castType, null)) {
        return new com_slime_java_ast_JavaAstCastExpression(com_slime_java_ast_JavaCstToAst.typeName(castType), com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.lastChild(node, "unaryExpression")));
      }
    }
    if (__QinJavaLangString.equals("postfixExpression", node.getName())) {
      return com_slime_java_ast_JavaCstToAst.lowerPostfixExpression(node);
    }
    if ((() => {
      if (__QinJavaLangString.equals("primary", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "NEW"), null);
      }
      return false;
    })()) {
      let arrayInitializer: any = com_slime_java_ast_JavaCstToAst.child(node, "arrayInitializer");
      if (__qin_binary__("!=", arrayInitializer, null)) {
        return new com_slime_java_ast_JavaAstArrayLiteralExpression(com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType")), com_slime_java_ast_JavaCstToAst.lowerArrayInitializer(arrayInitializer));
      }
      let arrayCreatorDimensions: any = com_slime_java_ast_JavaCstToAst.child(node, "arrayCreatorDimensions");
      if (__qin_binary__("!=", arrayCreatorDimensions, null)) {
        return new com_slime_java_ast_JavaAstArrayLiteralExpression(__qin_binary__("+", com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType")), "[]"), __QinJavaUtilList.of());
      }
      return new com_slime_java_ast_JavaAstNewExpression(com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType")), com_slime_java_ast_JavaCstToAst.lowerArguments(com_slime_java_ast_JavaCstToAst.child(node, "arguments")));
    }
    if ((() => {
      if (__QinJavaLangString.equals("primary", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "COLONCOLON"), null);
      }
      return false;
    })()) {
      let identifier: any = com_slime_java_ast_JavaCstToAst.child(node, "identifier");
      return new com_slime_java_ast_JavaAstMethodReferenceExpression(com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(node, "typeType")), (() => {
      if (__qin_binary__("==", identifier, null)) {
        return "new";
      }
      return com_slime_java_ast_JavaCstToAst.identifierValue(identifier);
    })());
    }
    if ((() => {
      if (__QinJavaLangString.equals("primary", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "CLASS"), null);
      }
      return false;
    })()) {
      return new com_slime_java_ast_JavaAstClassLiteralExpression(com_slime_java_ast_JavaCstToAst.typeNameOrVoid(com_slime_java_ast_JavaCstToAst.child(node, "typeTypeOrVoid")));
    }
    if ((() => {
      if ((() => {
      if (__QinJavaLangString.equals("primary", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "identifier"), null);
      }
      return false;
    })()) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "arguments"), null);
      }
      return false;
    })()) {
      return new com_slime_java_ast_JavaAstMethodCallExpression(new com_slime_java_ast_JavaAstThisExpression(), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(node, "identifier")), com_slime_java_ast_JavaCstToAst.lowerArguments(com_slime_java_ast_JavaCstToAst.child(node, "arguments")));
    }
    if ((() => {
      if (__QinJavaLangString.equals("primary", node.getName())) {
        return __qin_binary__("!=", com_slime_java_ast_JavaCstToAst.child(node, "THIS"), null);
      }
      return false;
    })()) {
      return new com_slime_java_ast_JavaAstThisExpression();
    }
    if (__QinJavaLangString.equals("identifier", node.getName())) {
      return new com_slime_java_ast_JavaAstIdentifierExpression(com_slime_java_ast_JavaCstToAst.identifierValue(node));
    }
    let children: any = com_slime_java_ast_JavaCstToAst.children(node);
    if (__qin_binary__("==", children.size(), 1.0)) {
      return com_slime_java_ast_JavaCstToAst.lowerExpression(children.get(0.0));
    }
    for (const child of children) {
      if ((() => {
      if (child.isToken()) {
        return false;
      }
      return true;
    })()) {
        return com_slime_java_ast_JavaCstToAst.lowerExpression(child);
      }
    }
    throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Unsupported expression node: ", node.getName()));
  }
  static lowerLambdaExpression(node: com_subhuti_struct_SubhutiCst): any {
    return new com_slime_java_ast_JavaAstLambdaExpression(com_slime_java_ast_JavaCstToAst.lowerLambdaParameterNames(com_slime_java_ast_JavaCstToAst.child(node, "lambdaParameters")), (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "lambdaBody"), "expression"), null)) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "lambdaBody"), "expression"));
    })(), (() => {
      if (__qin_binary__("==", com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "lambdaBody"), "block"), null)) {
        return __QinJavaUtilList.of();
      }
      return com_slime_java_ast_JavaCstToAst.lowerBlock(com_slime_java_ast_JavaCstToAst.child(com_slime_java_ast_JavaCstToAst.child(node, "lambdaBody"), "block"));
    })());
  }
  static lowerLambdaParameterNames(lambdaParameters: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", lambdaParameters, null)) {
      return __QinJavaUtilList.of();
    }
    let names: any = new __QinJavaUtilArrayList();
    for (const identifier of com_slime_java_ast_JavaCstToAst.children(lambdaParameters, "identifier")) {
      names.add(com_slime_java_ast_JavaCstToAst.identifierValue(identifier));
    }
    let formalParameterList: any = com_slime_java_ast_JavaCstToAst.child(lambdaParameters, "formalParameterList");
    for (const formalParameter of com_slime_java_ast_JavaCstToAst.children(formalParameterList, "formalParameter")) {
      names.add(com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(formalParameter, "variableDeclaratorId")));
    }
    return names;
  }
  static lowerLeftAssociative(node: com_subhuti_struct_SubhutiCst, operatorTokenNames: any): any {
    let children: any = com_slime_java_ast_JavaCstToAst.children(node);
    let current: any = null;
    let operator: any = null;
    for (const child of children) {
      if ((() => {
      if (child.isToken()) {
        return operatorTokenNames.contains(child.getName());
      }
      return false;
    })()) {
        operator = child.getValue();
        continue;
      }
      if (child.isToken()) {
        continue;
      }
      let expression: any = com_slime_java_ast_JavaCstToAst.lowerExpression(child);
      if (__qin_binary__("==", current, null)) {
        current = expression;
      } else {
        if (__qin_binary__("!=", operator, null)) {
          current = new com_slime_java_ast_JavaAstBinaryExpression(operator, current, expression);
          operator = null;
        }
      }
    }
    if (__qin_binary__("==", current, null)) {
      throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Expected expression under ", node.getName()));
    }
    return current;
  }
  static lowerRelationalExpression(node: com_subhuti_struct_SubhutiCst): any {
    let children: any = com_slime_java_ast_JavaCstToAst.children(node);
    let current: any = null;
    let operator: any = null;
    for (const child of children) {
      if ((() => {
      if (child.isToken()) {
        return __QinJavaUtilList.of("LT", "GT", "LE", "GE", "INSTANCEOF").contains(child.getName());
      }
      return false;
    })()) {
        operator = child.getValue();
        continue;
      }
      if (child.isToken()) {
        continue;
      }
      if (__QinJavaLangString.equals("pattern", child.getName())) {
        if ((() => {
      if (__qin_binary__("==", current, null)) {
        return true;
      }
      return (() => {
      if (__QinJavaLangString.equals("instanceof", operator)) {
        return false;
      }
      return true;
    })();
    })()) {
          throw new __QinJavaLangIllegalArgumentException("Unexpected Java pattern under relationalExpression");
        }
        current = new com_slime_java_ast_JavaAstInstanceofPatternExpression(current, com_slime_java_ast_JavaCstToAst.typeName(com_slime_java_ast_JavaCstToAst.child(child, "typeType")), com_slime_java_ast_JavaCstToAst.identifierValue(com_slime_java_ast_JavaCstToAst.child(child, "identifier")));
        operator = null;
        continue;
      }
      if ((() => {
      if (__QinJavaLangString.equals("typeType", child.getName())) {
        return __QinJavaLangString.equals("instanceof", operator);
      }
      return false;
    })()) {
        if (__qin_binary__("==", current, null)) {
          throw new __QinJavaLangIllegalArgumentException("Unexpected Java instanceof type under relationalExpression");
        }
        current = new com_slime_java_ast_JavaAstInstanceofExpression(current, com_slime_java_ast_JavaCstToAst.typeName(child));
        operator = null;
        continue;
      }
      let expression: any = com_slime_java_ast_JavaCstToAst.lowerExpression(child);
      if (__qin_binary__("==", current, null)) {
        current = expression;
      } else {
        if (__qin_binary__("!=", operator, null)) {
          current = new com_slime_java_ast_JavaAstBinaryExpression(operator, current, expression);
          operator = null;
        }
      }
    }
    if (__qin_binary__("==", current, null)) {
      throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Expected expression under ", node.getName()));
    }
    return current;
  }
  static assignmentOperatorValue(node: com_subhuti_struct_SubhutiCst): any {
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      if (child.isToken()) {
        return child.getValue();
      }
    }
    throw new __QinJavaLangIllegalArgumentException("Expected assignment operator token");
  }
  static prefixOperatorValue(node: com_subhuti_struct_SubhutiCst): any {
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      if (child.isToken()) {
        return child.getValue();
      }
    }
    throw new __QinJavaLangIllegalArgumentException("Expected prefix operator token");
  }
  static lowerPostfixExpression(node: com_subhuti_struct_SubhutiCst): any {
    let primary: any = com_slime_java_ast_JavaCstToAst.child(node, "primary");
    let current: any = com_slime_java_ast_JavaCstToAst.lowerExpression(primary);
    for (const selector of com_slime_java_ast_JavaCstToAst.children(node, "selector")) {
      current = com_slime_java_ast_JavaCstToAst.lowerSelector(current, selector);
    }
    for (const postfixOp of com_slime_java_ast_JavaCstToAst.children(node, "postfixOp")) {
      current = new com_slime_java_ast_JavaAstUpdateExpression(current, com_slime_java_ast_JavaCstToAst.updateOperatorValue(postfixOp), false);
    }
    return current;
  }
  static updateOperatorValue(node: com_subhuti_struct_SubhutiCst): any {
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      if (child.isToken()) {
        return child.getValue();
      }
    }
    throw new __QinJavaLangIllegalArgumentException("Expected update operator token");
  }
  static lowerSelector(receiver: com_slime_java_ast_JavaAstExpression, selector: com_subhuti_struct_SubhutiCst): any {
    let identifier: any = com_slime_java_ast_JavaCstToAst.child(selector, "identifier");
    if (__qin_binary__("!=", identifier, null)) {
      let __qin_arguments: any = com_slime_java_ast_JavaCstToAst.child(selector, "arguments");
      if (__qin_binary__("!=", __qin_arguments, null)) {
        return new com_slime_java_ast_JavaAstMethodCallExpression(receiver, com_slime_java_ast_JavaCstToAst.identifierValue(identifier), com_slime_java_ast_JavaCstToAst.lowerArguments(__qin_arguments));
      }
      return new com_slime_java_ast_JavaAstMemberAccessExpression(receiver, com_slime_java_ast_JavaCstToAst.identifierValue(identifier));
    }
    let explicitGenericInvocation: any = com_slime_java_ast_JavaCstToAst.child(selector, "explicitGenericInvocation");
    if (__qin_binary__("!=", explicitGenericInvocation, null)) {
      let suffix: any = com_slime_java_ast_JavaCstToAst.child(explicitGenericInvocation, "explicitGenericInvocationSuffix");
      let genericIdentifier: any = com_slime_java_ast_JavaCstToAst.child(suffix, "identifier");
      if (__qin_binary__("!=", genericIdentifier, null)) {
        return new com_slime_java_ast_JavaAstMethodCallExpression(receiver, com_slime_java_ast_JavaCstToAst.identifierValue(genericIdentifier), com_slime_java_ast_JavaCstToAst.lowerArguments(com_slime_java_ast_JavaCstToAst.child(suffix, "arguments")), com_slime_java_ast_JavaCstToAst.lowerNonWildcardTypeArguments(com_slime_java_ast_JavaCstToAst.child(explicitGenericInvocation, "nonWildcardTypeArguments")));
      }
    }
    let expression: any = com_slime_java_ast_JavaCstToAst.child(selector, "expression");
    if (__qin_binary__("!=", expression, null)) {
      return new com_slime_java_ast_JavaAstArrayAccessExpression(receiver, com_slime_java_ast_JavaCstToAst.lowerExpression(expression));
    }
    throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Unsupported Java selector: ", selector.toTreeString()));
  }
  static lowerArguments(__qin_arguments: com_subhuti_struct_SubhutiCst): any {
    let expressionList: any = com_slime_java_ast_JavaCstToAst.child(__qin_arguments, "expressionList");
    if (__qin_binary__("==", expressionList, null)) {
      return __QinJavaUtilList.of();
    }
    return com_slime_java_ast_JavaCstToAst.lowerExpressionList(expressionList);
  }
  static lowerExpressionList(expressionList: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", expressionList, null)) {
      return __QinJavaUtilList.of();
    }
    let lowered: any = new __QinJavaUtilArrayList();
    for (const expression of com_slime_java_ast_JavaCstToAst.children(expressionList, "expression")) {
      lowered.add(com_slime_java_ast_JavaCstToAst.lowerExpression(expression));
    }
    return lowered;
  }
  static lowerArrayInitializer(arrayInitializer: com_subhuti_struct_SubhutiCst): any {
    let elements: any = new __QinJavaUtilArrayList();
    for (const initializer of com_slime_java_ast_JavaCstToAst.children(arrayInitializer, "variableInitializer")) {
      let nestedArray: any = com_slime_java_ast_JavaCstToAst.child(initializer, "arrayInitializer");
      if (__qin_binary__("!=", nestedArray, null)) {
        elements.add(new com_slime_java_ast_JavaAstArrayLiteralExpression("Object[]", com_slime_java_ast_JavaCstToAst.lowerArrayInitializer(nestedArray)));
        continue;
      }
      elements.add(com_slime_java_ast_JavaCstToAst.lowerExpression(com_slime_java_ast_JavaCstToAst.child(initializer, "expression")));
    }
    return elements;
  }
  static numberValue(value: string): any {
    if ((() => {
      if (__qin_binary__("==", value, null)) {
        return true;
      }
      return __QinJavaLangString.isBlank(value);
    })()) {
      return 0.0;
    }
    return __QinJavaLangDouble.parseDouble(value.replace("_", ""));
  }
  static isIntegralNumberToken(tokenName: string): any {
    return (() => {
      if ((() => {
      if ((() => {
      if (__QinJavaLangString.equals("DECIMAL_LITERAL", tokenName)) {
        return true;
      }
      return __QinJavaLangString.equals("HEX_LITERAL", tokenName);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals("OCT_LITERAL", tokenName);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals("BINARY_LITERAL", tokenName);
    })();
  }
  static charLiteralText(value: string): any {
    let text: any = com_slime_java_ast_JavaCstToAst.unquoteString(value);
    if ((() => {
      if (__qin_binary__("==", text, null)) {
        return true;
      }
      return __QinJavaLangString.isEmpty(text);
    })()) {
      throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Empty Java char literal: ", value));
    }
    let first: any = text.codePointAt(0.0);
    if (__qin_binary__("==", __QinJavaLangCharacter.charCount(first), __QinJavaLangString.length(text))) {
      return text;
    }
    throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Invalid Java char literal: ", value));
  }
  static unquoteString(value: string): any {
    if ((() => {
      if (__qin_binary__("==", value, null)) {
        return true;
      }
      return __qin_binary__("<", __QinJavaLangString.length(value), 2.0);
    })()) {
      return value;
    }
    return com_slime_java_ast_JavaCstToAst.decodeJavaEscapes(__QinJavaLangString.substring(value, 1.0, __qin_binary__("-", __QinJavaLangString.length(value), 1.0)), value);
  }
  static decodeJavaEscapes(text: string, literal: string): any {
    let out: any = new __QinJavaLangStringBuilder(__QinJavaLangString.length(text));
    for (let index: any = 0.0; __qin_binary__("<", index, __QinJavaLangString.length(text)); index++) {
      let ch: any = __QinJavaLangString.charAt(text, index);
      if (__qin_binary__("!=", ch, "\\")) {
        out.append(ch);
        continue;
      }
      if (__qin_binary__(">=", __qin_binary__("+", index, 1.0), __QinJavaLangString.length(text))) {
        throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Invalid Java string literal: ", literal));
      }
      let escape: any = __QinJavaLangString.charAt(text, ++index);
      switch (escape) {
        case "b":
          out.append("\b");
        case "t":
          out.append("\t");
        case "n":
          out.append("\n");
        case "f":
          out.append("\f");
        case "r":
          out.append("\r");
        case "\"":
          out.append("\"");
        case "'":
          out.append("'");
        case "\\":
          out.append("\\");
        case "u":
          while ((() => {
      if (__qin_binary__("<", __qin_binary__("+", index, 1.0), __QinJavaLangString.length(text))) {
        return __qin_binary__("==", __QinJavaLangString.charAt(text, __qin_binary__("+", index, 1.0)), "u");
      }
      return false;
    })()) {
            index++;
          }
          if (__qin_binary__(">=", __qin_binary__("+", index, 4.0), __QinJavaLangString.length(text))) {
            throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Invalid Java unicode escape: ", literal));
          }
          out.append((__QinJavaLangInteger.parseInt(__QinJavaLangString.substring(text, __qin_binary__("+", index, 1.0), __qin_binary__("+", index, 5.0)), 16.0) | 0));
          index += 4.0;
        default:
          if ((() => {
      if (__qin_binary__("<", escape, "0")) {
        return true;
      }
      return __qin_binary__(">", escape, "7");
    })()) {
            throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Invalid Java escape sequence: ", literal));
          }
          let end: any = index;
          let maxEnd: any = (() => {
      if (__qin_binary__("<=", escape, "3")) {
        return Math.min(__qin_binary__("+", index, 2.0), __qin_binary__("-", __QinJavaLangString.length(text), 1.0));
      }
      return Math.min(__qin_binary__("+", index, 1.0), __qin_binary__("-", __QinJavaLangString.length(text), 1.0));
    })();
          while (__qin_binary__("<", end, maxEnd)) {
            let digit: any = __QinJavaLangString.charAt(text, __qin_binary__("+", end, 1.0));
            if ((() => {
      if (__qin_binary__("<", digit, "0")) {
        return true;
      }
      return __qin_binary__(">", digit, "7");
    })()) {
              break;
            }
            end++;
          }
          out.append((__QinJavaLangInteger.parseInt(__QinJavaLangString.substring(text, index, __qin_binary__("+", end, 1.0)), 8.0) | 0));
          index = end;
      }
    }
    return out.toString();
  }
  static qualifiedName(node: com_subhuti_struct_SubhutiCst): any {
    let qualifiedName: any = com_slime_java_ast_JavaCstToAst.child(node, "qualifiedName");
    if (__qin_binary__("==", qualifiedName, null)) {
      qualifiedName = com_slime_java_ast_JavaCstToAst.child(node, "importQualifiedName");
    }
    return com_slime_java_ast_JavaCstToAst.tokenText((() => {
      if (__qin_binary__("==", qualifiedName, null)) {
        return node;
      }
      return qualifiedName;
    })());
  }
  static lowerAnnotations(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", node, null)) {
      return __QinJavaUtilList.of();
    }
    let annotations: any = new __QinJavaUtilArrayList();
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      let name: any = child.getName();
      if ((() => {
      if ((() => {
      if ((() => {
      if (__QinJavaLangString.equals("classOrInterfaceModifier", name)) {
        return false;
      }
      return true;
    })()) {
        return (() => {
      if (__QinJavaLangString.equals("modifier", name)) {
        return false;
      }
      return true;
    })();
      }
      return false;
    })()) {
        return (() => {
      if (__QinJavaLangString.equals("variableModifier", name)) {
        return false;
      }
      return true;
    })();
      }
      return false;
    })()) {
        continue;
      }
      let annotation: any = com_slime_java_ast_JavaCstToAst.findFirst(child, "annotation");
      if (__qin_binary__("!=", annotation, null)) {
        annotations.add(new com_slime_java_ast_JavaAstAnnotation(com_slime_java_ast_JavaCstToAst.qualifiedName(annotation)));
      }
    }
    return annotations;
  }
  static typeNameOrVoid(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", node, null)) {
      return "";
    }
    let typeType: any = com_slime_java_ast_JavaCstToAst.child(node, "typeType");
    if (__qin_binary__("!=", typeType, null)) {
      return com_slime_java_ast_JavaCstToAst.typeName(typeType);
    }
    let voidToken: any = com_slime_java_ast_JavaCstToAst.child(node, "VOID");
    return (() => {
      if (__qin_binary__("==", voidToken, null)) {
        return com_slime_java_ast_JavaCstToAst.tokenText(node);
      }
      return voidToken.getValue();
    })();
  }
  static typeName(node: com_subhuti_struct_SubhutiCst): any {
    return com_slime_java_ast_JavaCstToAst.tokenText(node);
  }
  static identifierValue(node: com_subhuti_struct_SubhutiCst): any {
    let token: any = com_slime_java_ast_JavaCstToAst.firstToken(node, "IDENTIFIER");
    if (__qin_binary__("==", token, null)) {
      token = com_slime_java_ast_JavaCstToAst.firstLeafToken(node);
    }
    if (__qin_binary__("==", token, null)) {
      throw new __QinJavaLangIllegalArgumentException(__qin_binary__("+", "Expected identifier token under ", (() => {
      if (__qin_binary__("==", node, null)) {
        return "null";
      }
      return node.getName();
    })()));
    }
    return token.getValue();
  }
  static tokenText(node: com_subhuti_struct_SubhutiCst): any {
    let builder: any = new __QinJavaLangStringBuilder();
    com_slime_java_ast_JavaCstToAst.appendTokenText(node, builder);
    return builder.toString();
  }
  static appendTokenText(node: com_subhuti_struct_SubhutiCst, builder: any): any {
    if (__qin_binary__("==", node, null)) {
      return null;
    }
    if (node.isToken()) {
      builder.append(node.getValue());
      return null;
    }
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      com_slime_java_ast_JavaCstToAst.appendTokenText(child, builder);
    }
    return null;
  }
  static hasTokenValue(node: com_subhuti_struct_SubhutiCst, value: string): any {
    if (__qin_binary__("==", node, null)) {
      return false;
    }
    if (node.isToken()) {
      return __QinJavaLangString.equals(value, node.getValue());
    }
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      if (com_slime_java_ast_JavaCstToAst.hasTokenValue(child, value)) {
        return true;
      }
    }
    return false;
  }
  static firstToken(node: com_subhuti_struct_SubhutiCst, tokenName: string): any {
    if (__qin_binary__("==", node, null)) {
      return null;
    }
    if ((() => {
      if (node.isToken()) {
        return __QinJavaLangString.equals(tokenName, node.getName());
      }
      return false;
    })()) {
      return node;
    }
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      let found: any = com_slime_java_ast_JavaCstToAst.firstToken(child, tokenName);
      if (__qin_binary__("!=", found, null)) {
        return found;
      }
    }
    return null;
  }
  static firstLeafToken(node: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", node, null)) {
      return null;
    }
    if (node.isToken()) {
      return node;
    }
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      let found: any = com_slime_java_ast_JavaCstToAst.firstLeafToken(child);
      if (__qin_binary__("!=", found, null)) {
        return found;
      }
    }
    return null;
  }
  static findFirst(node: com_subhuti_struct_SubhutiCst, name: string): any {
    if (__qin_binary__("==", node, null)) {
      return null;
    }
    if (__QinJavaLangString.equals(name, node.getName())) {
      return node;
    }
    for (const child of com_slime_java_ast_JavaCstToAst.children(node)) {
      let found: any = com_slime_java_ast_JavaCstToAst.findFirst(child, name);
      if (__qin_binary__("!=", found, null)) {
        return found;
      }
    }
    return null;
  }
  static child(node: com_subhuti_struct_SubhutiCst, name: string): any {
    return (() => {
      if (__qin_binary__("==", node, null)) {
        return null;
      }
      return node.getChild(name);
    })();
  }
  static lastChild(node: com_subhuti_struct_SubhutiCst, name: string): any {
    return (() => {
      if (com_slime_java_ast_JavaCstToAst.children(node, name).isEmpty()) {
        return null;
      }
      return com_slime_java_ast_JavaCstToAst.children(node, name).get(__qin_binary__("-", com_slime_java_ast_JavaCstToAst.children(node, name).size(), 1.0));
    })();
  }
  static children(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_struct_SubhutiCst)) && (__qin_args[1] === null || typeof __qin_args[1] === "string")) return this.__qin_overload_children_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_struct_SubhutiCst))) return this.__qin_overload_children_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: children/" + __qin_args.length);
  }
  static __qin_overload_children_2_0(node: com_subhuti_struct_SubhutiCst, name: string): any {
    return (() => {
      if (__qin_binary__("==", node, null)) {
        return __QinJavaUtilList.of();
      }
      return node.getChildren(name);
    })();
  }
  static __qin_overload_children_1_1(node: com_subhuti_struct_SubhutiCst): any {
    return (() => {
      if ((() => {
      if (__qin_binary__("==", node, null)) {
        return true;
      }
      return __qin_binary__("==", node.getChildren(), null);
    })()) {
        return __QinJavaUtilList.of();
      }
      return node.getChildren();
    })();
  }
  static currentTokenInfo(parser: com_slime_java_JavaParser): any {
    let token: any = parser.curToken();
    if (__qin_binary__("==", token, null)) {
      return "EOF";
    }
    return __qin_binary__("+", __qin_binary__("+", __qin_binary__("+", token.getTokenName(), " '"), token.getTokenValue()), "'");
  }
  static isAtEof(parser: com_slime_java_JavaParser): any {
    return (() => {
      if (__qin_binary__("==", parser.curToken(), null)) {
        return true;
      }
      return parser.curToken().isEof();
    })();
  }
}
const JavaCstToAst = com_slime_java_ast_JavaCstToAst;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_java_ast_JavaCstToAst };
