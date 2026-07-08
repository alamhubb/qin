package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinOvsParserVariantProfileProbeMain {
    private QinOvsParserVariantProfileProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }

        System.setProperty("qin.profile", "true");
        System.clearProperty("qin.ovs.parserProfile");
        QinOvsCompiler compiler = new QinOvsCompiler();
        String marker = "\n/* qin-variant-profile-marker=" + System.nanoTime() + " */\n";

        runCase(compiler, root, "empty_export", """
                export const Probe = () => {
                    return "x"
                }
                """ + marker);
        runCase(compiler, root, "bare_tr", """
                export const Probe = (row) => {
                    return tr { "x" }
                }
                """ + marker);
        runCase(compiler, root, "tr_props", """
                const rowKey = (row) => {
                    return row.id || row.keyPreview || row.domain
                }

                export const Probe = (row) => {
                    return tr(
                        key = rowKey(row);
                        class = "clickable-row";
                        tabindex = "0"
                    ) { "x" }
                }
                """ + marker);
        runCase(compiler, root, "tr_method_prop", """
                const openRootUrl = (row) => {
                    window.open(row.id)
                }

                export const Probe = (row) => {
                    return tr(
                        onClick() {
                            openRootUrl(row)
                        }
                    ) { "x" }
                }
                """ + marker);
        runCase(compiler, root, "tr_keydown_method", """
                const openRootUrl = (row) => {
                    window.open(row.id)
                }

                export const Probe = (row) => {
                    return tr(
                        onKeydown(event) {
                            if (event.key === "Enter" || event.key === " ") {
                                event.preventDefault()
                                openRootUrl(row)
                            }
                        }
                    ) { "x" }
                }
                """ + marker);
        runCase(compiler, root, "balance_first_cells", """
                const balanceText = (row) => row.balance

                export const Probe = (row) => {
                    return tr {
                        td { span(class = "table-strong") { row.domain || "-" } }
                        td {
                            div(class = "account-cell") {
                                strong { row.baseUrl || row.name || "-" }
                                span { (row.accountCount || 1) + " key(s) under this root URL" }
                            }
                        }
                        td { span(class = "mono") { row.keyPreview || "-" } }
                        td { span(class = "balance-cell") { balanceText(row) } }
                    }
                }
                """ + marker);
        runCase(compiler, root, "balance_like_full_shape", """
                const statusClass = (row) => row.status
                const balanceText = (row) => row.balance
                const Sparkline = (row) => span { row.spark }
                const Badge = (props) => span { props.label }

                export const Probe = (row) => {
                    return tr(
                        key = row.id || row.keyPreview || row.domain;
                        class = "clickable-row";
                        tabindex = "0";
                        onClick() {
                            window.open(row.id)
                        };
                        onKeydown(event) {
                            if (event.key === "Enter" || event.key === " ") {
                                event.preventDefault()
                                window.open(row.id)
                            }
                        }
                    ) {
                        td { span(class = "table-strong") { row.domain || "-" } }
                        td {
                            div(class = "account-cell") {
                                strong { row.baseUrl || row.name || "-" }
                                span { (row.accountCount || 1) + " key(s) under this root URL" }
                            }
                        }
                        td { span(class = "mono") { row.keyPreview || "-" } }
                        td { span(class = "balance-cell") { balanceText(row) } }
                        td {
                            div(class = "rate-cell") {
                                strong { row.effectiveRate ? "x" + row.effectiveRate : "-" }
                                span { row.groupName || row.rateSource || row.rateError || "" }
                            }
                        }
                        td { Sparkline(row) }
                        td {
                            Badge({
                                label: row.status || "-",
                                tone: row.status === "ok" ? statusClass(row) : "destructive"
                            })
                        }
                        td { span(class = "mono") { row.path || "-" } }
                        td { span(class = "error-cell") { row.error || "-" } }
                    }
                }
                """ + marker);
    }

    private static void runCase(QinOvsCompiler compiler, Path root, String name, String source) throws Exception {
        Map<Path, String> modules = new LinkedHashMap<>();
        modules.put(root.resolve("app").resolve("__probe_" + name + ".ovs"), source);
        long started = System.nanoTime();
        try {
            compiler.compileAll(root, modules);
            long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
            System.out.println("[QinOvsParserVariantProfileProbe] " + name + " status=ok elapsedMs=" + elapsedMs);
        } catch (Exception error) {
            long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
            String message = error.getMessage() == null ? error.toString() : error.getMessage();
            System.out.println("[QinOvsParserVariantProfileProbe] " + name
                    + " status=error elapsedMs=" + elapsedMs
                    + " message=" + abbreviate(message));
        }
    }

    private static String abbreviate(String value) {
        return value.length() <= 240 ? value : value.substring(0, 240) + "...";
    }
}
