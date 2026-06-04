package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeRef;

record QinJavaInheritedField(
        String name,
        QinIrTypeRef type,
        String ownerBinaryName,
        boolean staticField) {
}
