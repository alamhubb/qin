package com.qin.debug.lsp;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

record QinLspServerCommandSpec(
        String executable,
        List<String> arguments,
        Path workDirectory,
        Charset charset,
        Map<String, String> environment) {
}
