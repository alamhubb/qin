package com.qin.lang.pipeline.cfa;

/**
 * Unified compile contract for CFA-oriented Qin pipeline.
 */
public interface QinCfaPipeline {
    QinCfaCompileResult compile(QinCfaCompileRequest request) throws Exception;
}
