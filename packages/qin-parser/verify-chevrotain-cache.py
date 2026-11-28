"""Exercise cache admission without mutating installed parser artifacts."""
import importlib.util
import json
from pathlib import Path
import tempfile

spec = importlib.util.spec_from_file_location("qin_chevrotain_build", Path(__file__).with_name("build-chevrotain-parser.py"))
builder = importlib.util.module_from_spec(spec)
spec.loader.exec_module(builder)
manifest = builder.input_hashes()
assert builder.cache_valid(manifest), "Build the current parser before running this proof"
builder.BUILD = Path(tempfile.mkdtemp(prefix="cache-proof-", dir=builder.BUILD))
classes = builder.BUILD / "classes"
classes.mkdir()
sample = classes / "Sample.class"
sample.write_bytes(b"original")
jar = builder.BUILD / "qin-parser.jar"
jar.write_bytes(b"jar")
report = {"backend": "JAVA_CHEVROTAIN", "manifest": manifest,
          "outputs": {str(path.relative_to(builder.BUILD)): builder.digest(path) for path in (sample, jar)}}
(builder.BUILD / "chevrotain-runtime.json").write_bytes(json.dumps(report).encode("utf-8"))
assert builder.cache_valid(manifest)
sample.write_bytes(b"changed")
assert not builder.cache_valid(manifest)
sample.unlink()
assert not builder.cache_valid(manifest)
sample.write_bytes(b"original")
extra = classes / "Unexpected.class"
extra.write_bytes(b"extra")
assert not builder.cache_valid(manifest)
extra.unlink()
assert not builder.cache_valid({**manifest, "javac": "changed"})
assert not builder.cache_valid({**manifest, "publicParser": "changed"})
assert builder.cache_valid(manifest)
print(f"QIN_CACHE_PROOF_OK cases=8 evidence={builder.BUILD}")
