"""Build the existing Qin/CSSTS/OVS author grammars with Slime's Java Chevrotain backend."""
import contextlib
import hashlib
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
import zipfile

ROOT = Path(__file__).resolve().parent
SLIME = ROOT.parents[2] / "slime"
JAVA_SLIME = SLIME / "java-slime"
FRONTEND = JAVA_SLIME / "slime-runtime-slime"
CANDIDATE = JAVA_SLIME / "slime-chevrotain-candidate"
BUILD = ROOT / "build"
SOURCE = ROOT / "src/java/com/qin/parser"
IR = ROOT.parent / "qin-lang-ir/build/classes"
JVM = ["-Xmx1g", "-XX:-UseJVMCICompiler", "-Dfile.encoding=UTF-8",
       "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8"]
PACKED_LOOKAHEAD = (CANDIDATE / "src/main/java/com/subhuti/chevrotain/direct/chevrotain/parse"
                    / "grammar/PackedLookaheadStrategy.java")
STABLE_SOURCE_LEXER = [
    JAVA_SLIME / "subhuti-java/src/main/java/com/subhuti/lexer/SubhutiLexer.java",
    CANDIDATE / "src/integration/java/com/slime/chevrotain/SlimeSourceInput.java",
]
sys.path.insert(0, str(CANDIDATE))
from verify_step04 import GRAMMAR_SUPPORT
from package_candidate_jar import (GENERATED_RULE_EXECUTION_PATCH, LOOKAHEAD_PATCH, LOOKAHEAD_SOURCES,
                                   PACKAGED_DIRECT_PARITY_PATCHES, RECORDED_GRAMMAR_PATCH,
                                   STABLE_LOOKAHEAD_PATCH, STRICT_DIAGNOSTIC_PATCH,
                                   UNTRACED_LOOKAHEAD_PATCH, compile_parser_core)
sys.path.insert(0, str(FRONTEND / "tools"))
import importlib.util
public_spec = importlib.util.spec_from_file_location("slime_public_builder", FRONTEND / "tools/build-public-parser.py")
public_builder = importlib.util.module_from_spec(public_spec)
public_spec.loader.exec_module(public_builder)


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def run(command, out, label):
    result = subprocess.run([str(item) for item in command], cwd=ROOT,
                            capture_output=True, creationflags=getattr(subprocess, "CREATE_NO_WINDOW", 0))
    (out / (label + ".log")).write_bytes(result.stdout + result.stderr)
    if result.returncode:
        raise RuntimeError(f"{label} failed, see {out / (label + '.log')}\n"
                           + (result.stdout + result.stderr).decode("utf-8", errors="replace")[-6000:])
    return result.stdout.decode("utf-8", errors="replace")


def classpath(*paths):
    return os.pathsep.join(str(path) for path in paths)


def compile_java(out, destination, sources, cp=""):
    destination.mkdir(parents=True, exist_ok=True)
    argfile = out / (destination.name + ".args")
    argfile.write_bytes("".join(f'"{path.as_posix()}"\n' for path in sources).encode("utf-8"))
    run(["javac", "-J-Xmx1g", "-encoding", "UTF-8", "--release", "25",
         "-cp", cp, "-d", destination, "@" + str(argfile)], out, destination.name + "-compile")


@contextlib.contextmanager
def build_lock():
    BUILD.mkdir(parents=True, exist_ok=True)
    with (BUILD / "chevrotain-build.lock").open("a+b") as stream:
        if os.name == "nt":
            import msvcrt
            stream.seek(0)
            stream.write(b"0")
            stream.flush()
            stream.seek(0)
            while True:
                try:
                    msvcrt.locking(stream.fileno(), msvcrt.LK_NBLCK, 1)
                    break
                except OSError:
                    import time
                    time.sleep(0.2)
        else:
            import fcntl
            fcntl.flock(stream.fileno(), fcntl.LOCK_EX)
        try:
            yield
        finally:
            if os.name == "nt":
                stream.seek(0)
                msvcrt.locking(stream.fileno(), msvcrt.LK_UNLCK, 1)


def input_hashes():
    paths = {Path(__file__), ROOT / "qin.config.js"}
    for folder, glob in [(SOURCE, "*.java"), (IR, "*.class"),
                         (FRONTEND / "src/compiler/java", "*.java"),
                         (FRONTEND / "tools", "*.py"),
                         (CANDIDATE / "src/tool/java", "*.java"),
                         (CANDIDATE / "src/public/java", "*.java")]:
        paths.update(folder.rglob(glob))
    for module in ("subhuti-java", "slime-token", "slime-parser", "slime-ast", "slime-runtime", "slime-generator"):
        paths.update((JAVA_SLIME / module / "src/main/java").rglob("*.java"))
    paths.update(GRAMMAR_SUPPORT)
    paths.update(LOOKAHEAD_SOURCES)
    paths.update(STABLE_SOURCE_LEXER)
    paths.update(CANDIDATE.glob("*.py"))
    paths.update((*PACKAGED_DIRECT_PARITY_PATCHES, LOOKAHEAD_PATCH, STABLE_LOOKAHEAD_PATCH,
                  STRICT_DIAGNOSTIC_PATCH, GENERATED_RULE_EXECUTION_PATCH, RECORDED_GRAMMAR_PATCH,
                  UNTRACED_LOOKAHEAD_PATCH,
                  CANDIDATE / "src/main/java/com/slime/chevrotain/RecordedGrammar.java", PACKED_LOOKAHEAD))
    versions = subprocess.run(["javac", "-version"], capture_output=True, check=True)
    return {"inputs": {str(path): digest(path) for path in sorted(paths)},
            "javac": (versions.stdout + versions.stderr).decode("utf-8"),
            "publicParser": public_builder.fingerprint()[0]}


def cache_valid(manifest):
    try:
        report = json.loads((BUILD / "chevrotain-runtime.json").read_bytes())
        return (report["manifest"] == manifest
                and report["backend"] == "JAVA_CHEVROTAIN"
                and set(report["outputs"]) == {"qin-parser.jar", *(
                    str(path.relative_to(BUILD)) for path in (BUILD / "classes").rglob("*") if path.is_file())}
                and all(digest(BUILD / path) == sha for path, sha in report["outputs"].items()))
    except (OSError, ValueError, KeyError):
        return False


def facade(name, parent, fingerprint):
    initialize_backend = "backend(true);" if name == "OvsParser" else ""
    return f'''package com.qin.parser;
import com.qin.parser.generated.{name}ProgramParser;
public class {name} extends {parent} {{
    public static final String BACKEND_FINGERPRINT = "{fingerprint}";
    public {name}(String source) {{ super(source); {initialize_backend} }}
    @Override protected Backend backend(boolean parameterized) {{ return Holder.BACKEND; }}
    private static final class Holder {{
        static final com.slime.chevrotain.StableTokenInput.Vocabulary VOCABULARY =
            loadVocabulary({name}.class, "/com/qin/parser/generated/{name}.tokens.tsv");
        static final {name}ProgramParser PARSER = new {name}ProgramParser(VOCABULARY);
        static final Backend BACKEND = new Backend(VOCABULARY, PARSER, PARSER::parse);
    }}
    public void QinModule(SourceType sourceType) {{ Program(sourceType); }}
}}
'''


def build(manifest):
    out = Path(tempfile.mkdtemp(prefix="chevrotain-", dir=BUILD))
    print(f"QIN_CHEVROTAIN_BUILD={out}", flush=True)
    public_output = run([sys.executable, "-X", "utf8", FRONTEND / "tools/build-public-parser.py", "--verify"],
                        out, "public-parser")
    public_jar = Path(next(line.split("=", 1)[1] for line in public_output.splitlines()
                           if line.startswith("PUBLIC_PARSER_JAR=")))
    inventory = out / "inventory"
    run([sys.executable, "-X", "utf8", FRONTEND / "tools/run.py", "--inventory", "--output", inventory],
        out, "inventory")
    author_classes = inventory / "classes"
    author_sources = [Path(line.strip('"')) for line in (inventory / "sources.txt").read_text(encoding="utf-8").splitlines()]
    authors = [path for path in author_sources if "slime-parser/src/main/java/com/slime/parser/" in path.as_posix()
               and "/cstToAst/" not in path.as_posix() and not path.name.endswith("StaticGrammar.java")]
    authors += [JAVA_SLIME / "subhuti-java/src/main/java/com/subhuti/lookahead/SubhutiTokenLookahead.java"]
    authors += [SOURCE / (name + ".java") for name in ("QinParser", "CssTsParser", "OvsParser")]
    authors_list = out / "authors.list"
    authors_list.write_bytes(("\n".join(path.as_posix() for path in authors) + "\n").encode("utf-8"))
    compiler = out / "compiler"
    compile_java(out, compiler, sorted((FRONTEND / "src/compiler/java").rglob("*.java")), str(author_classes))
    support = out / "support"
    compile_java(out, support, GRAMMAR_SUPPORT)
    tools = out / "tools"
    recorded_grammar_source = CANDIDATE / "src/main/java/com/slime/chevrotain/RecordedGrammar.java"
    tool_sources = [CANDIDATE / "src/tool/java" / path for path in (
        "com/slime/chevrotain/JavaParserSourceGenerator.java", "com/slime/chevrotain/Step05GenerateParserMain.java",
        "com/slime/chevrotain/LookaheadBlueprintCaptureMain.java",
        "com/subhuti/chevrotain/direct/chevrotain/parse/grammar/LookaheadBlueprintCaptureStrategy.java")]
    compile_java(out, tools, [*tool_sources, recorded_grammar_source, PACKED_LOOKAHEAD],
                 classpath(public_jar, support))
    classes = out / "classes"
    classes.mkdir()
    with zipfile.ZipFile(public_jar) as archive:
        archive.extractall(classes)
    # Keep Slime's signed-off base JAR intact. Build the generated-grammar API for this Qin JAR.
    core_output = Path(tempfile.mkdtemp(prefix="qin-recorded-grammar-", dir=SLIME / "build"))
    core_classes, _ = compile_parser_core(core_output, recorded_grammar=True, classpath=str(public_jar))
    shutil.copytree(core_classes, classes, dirs_exist_ok=True)
    compile_java(out, classes, [recorded_grammar_source, PACKED_LOOKAHEAD, *LOOKAHEAD_SOURCES], str(classes))
    runtime_sources = out / "runtime-sources/com/qin/parser"
    runtime_sources.mkdir(parents=True)
    fingerprint = hashlib.sha256(json.dumps(manifest, sort_keys=True).encode("utf-8")).hexdigest()
    for name, parent in (("QinParser", "com.slime.parser.SlimeParser"), ("CssTsParser", "QinParser"), ("OvsParser", "CssTsParser")):
        module = out / (name + "Grammar.java")
        module_class = "com.qin.parser.generated." + name + "Grammar"
        print(f"QIN_CHEVROTAIN_GENERATING={name}", flush=True)
        run(["java", *JVM, "-cp", classpath(compiler, author_classes),
             "com.slime.runtime.slime.compiler.AuthoringCompiler", "--collect-unified-reachable", SLIME,
             authors_list, author_classes, "com.qin.parser." + name,
             "Program(com.slime.parser.SlimeJavascriptParser.SourceType)", module_class, module], out, name + "-author")
        module_classes = out / (name + "-module")
        compile_java(out, module_classes, [module], classpath(support, author_classes))
        generator_cp = classpath(tools, support, public_jar, module_classes)
        parser_name = name + "ProgramParser"
        source = out / "capture" / (parser_name + ".java")
        command = ["java", *JVM, "-cp", generator_cp, "com.slime.chevrotain.Step05GenerateParserMain",
                   module, module_class, "com.qin.parser.generated", parser_name]
        run([*command, source, "--release-lookahead", "--capture-lookahead"], out, name + "-capture-generate")
        capture_classes = out / (name + "-capture")
        compile_java(out, capture_classes, [source], classpath(tools, public_jar))
        resources = classes / "com/qin/parser/generated"
        resources.mkdir(parents=True, exist_ok=True)
        blueprint = resources / (parser_name + ".lookahead.bin")
        recorded_grammar = resources / (parser_name + ".gast.bin")
        run(["java", *JVM, "-cp", classpath(capture_classes, tools, public_jar),
             "com.slime.chevrotain.LookaheadBlueprintCaptureMain", "com.qin.parser.generated." + parser_name,
             Path(str(source) + ".tokens.tsv"), blueprint, recorded_grammar], out, name + "-lookahead")
        final_source = out / "generated" / (parser_name + ".java")
        run([*command, final_source, "--release-lookahead", "--precomputed-lookahead", "--recorded-grammar"],
            out, name + "-generate")
        compile_java(out, classes, [final_source], str(classes))
        shutil.copyfile(Path(str(final_source) + ".tokens.tsv"), resources / (name + ".tokens.tsv"))
        (runtime_sources / (name + ".java")).write_bytes(facade(name, parent, fingerprint).encode("utf-8"))
    runtime_names = ["QinParserFacade", "QinProgramCstToAst", "QinParsedSource", "QinParserRuntimeNames",
                     "CsstsAtomNames", "OvsSourceScopes", "QinOvsCstToAst"]
    for name in runtime_names:
        shutil.copyfile(SOURCE / (name + ".java"), runtime_sources / (name + ".java"))
    # Compile Qin's visitor against the Slime sources it extends, including when the public JAR
    # contains older copies of these classes.
    slime_visitor = JAVA_SLIME / "slime-parser/src/main/java/com/slime/parser/cstToAst"
    slime_token = JAVA_SLIME / "slime-token/src/main/java/com/slime/token"
    shared_sources = [
        *STABLE_SOURCE_LEXER,
        slime_token / "JavaScriptTokenPattern.java",
        slime_token / "JavaScriptTokens.java",
        slime_visitor / "SlimeCstToAstUtils.java",
        slime_visitor / "expressions/SlimeExpressionCstToAst.java",
        slime_visitor / "typescript/SlimeTSExpressionCstToAst.java",
        slime_visitor / "components/SlimeFunctionParameterCstToAst.java",
        slime_visitor / "module/SlimeImportCstToAst.java",
        slime_visitor / "module/SlimeExportCstToAst.java",
        slime_visitor / "statements/SlimeVariableCstToAst.java",
        JAVA_SLIME / "slime-ast/src/main/java/com/slime/ast/nodes/modules/ExportAllDeclaration.java",
        JAVA_SLIME / "slime-ast/src/main/java/com/slime/ast/TypeOnlyDeclarations.java",
        JAVA_SLIME / "slime-ast/src/main/java/com/slime/ast/nodes/misc/FunctionParameter.java",
        JAVA_SLIME / "slime-ast/src/main/java/com/slime/ast/nodes/typescript/TSTypeAssertion.java",
        JAVA_SLIME / "slime-generator/src/main/java/com/slime/generator/SlimeGenerator.java",
        JAVA_SLIME / "slime-generator/src/main/java/com/slime/generator/EnumConstantValues.java",
    ]
    compile_java(out, classes, [*shared_sources, *runtime_sources.glob("*.java")], classpath(classes, IR))
    test_names = [
        "OvsParserJavaSmokeTestMain",
        "QinParserFacadeUnifiedEntrySmokeTestMain",
        "QinParserObjectDeclarationSmokeTestMain",
        "QinParserDefaultLimitsTestMain",
    ]
    tests = [SOURCE / (name + ".java") for name in test_names]
    test_classes = out / "test-classes"
    compile_java(out, test_classes, tests, classpath(classes, IR))
    for name in test_names:
        print(run(["java", *JVM, "-cp", classpath(test_classes, classes, IR), "com.qin.parser." + name],
                  out, name).strip(), flush=True)
    if input_hashes() != manifest:
        raise RuntimeError("Build inputs changed during generation, rerun with stable sources")
    jar = out / "qin-parser.jar"
    run(["jar", "--create", "--file", jar, "-C", classes, "."], out, "jar")
    destination = BUILD / "classes"
    previous = out / "previous-classes"
    if destination.exists():
        destination.rename(previous)
    try:
        classes.rename(destination)
        os.replace(jar, BUILD / "qin-parser.jar")
    except BaseException:
        if previous.exists() and not destination.exists():
            previous.rename(destination)
        raise
    outputs = {str(path.relative_to(BUILD)): digest(path) for path in destination.rglob("*") if path.is_file()}
    outputs["qin-parser.jar"] = digest(BUILD / "qin-parser.jar")
    report = {"backend": "JAVA_CHEVROTAIN", "manifest": manifest, "outputs": outputs,
              "evidence": str(out), "precomputedLookahead": True, "recordedGrammar": True}
    pending = out / "report.json"
    pending.write_bytes((json.dumps(report, indent=2) + "\n").encode("utf-8"))
    os.replace(pending, BUILD / "chevrotain-runtime.json")
    print("QIN_CHEVROTAIN_BUILD=PASSED", flush=True)


def main():
    with build_lock():
        manifest = input_hashes()
        if cache_valid(manifest):
            print("QIN_CHEVROTAIN_CACHE=HIT backend=JAVA_CHEVROTAIN")
            return
        build(manifest)


if __name__ == "__main__":
    main()
