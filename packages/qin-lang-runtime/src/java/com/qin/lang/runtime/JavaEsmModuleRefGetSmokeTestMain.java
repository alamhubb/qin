package com.qin.lang.runtime;

public final class JavaEsmModuleRefGetSmokeTestMain {
    private static Object exportSlot;

    private JavaEsmModuleRefGetSmokeTestMain() {
    }

    public static void main(String[] args) {
        String refName = "__qesm_m0_e_value";
        String localName = "value";
        JavaEsmGlobal.__qin_bind_module_ref__(localName, "exportSlot");
        JavaEsmGlobal.__qin_bind_module_ref__(refName, "exportSlot");
        exportSlot = JavaEsmGlobal.__qin_export_slot__();
        JavaEsmGlobal.__qin_export_init__(exportSlot, "ok");
        JavaEsmGlobal.__qin_mark_module_ref_initialized__(localName, "exportSlot");

        Object value = JavaEsmGlobal.__qin_module_ref_get__(refName);
        if (!"ok".equals(value)) {
            throw new IllegalStateException("Expected module ref value ok, got: " + value);
        }

        System.out.println("JavaEsmModuleRefGetSmokeTestMain OK");
    }
}
