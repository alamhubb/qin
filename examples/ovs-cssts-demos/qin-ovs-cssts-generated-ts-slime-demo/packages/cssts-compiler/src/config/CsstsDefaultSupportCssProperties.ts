import {CssPropertyName} from "./types/cssPropertyConfig";

export const atomicCssProperties: CssPropertyName[] = [
    // ==================== 布局 (Layout) ====================
    'display',
    'visibility',
    'position',
    'zIndex',

    // 坐标 (Coordinates)
    'inset',
    'top',
    'right',
    'bottom',
    'left',

    // 溢出 (Overflow)
    'overflow', // [加回] 同时设XY
    'overflowX',
    'overflowY',

    // ==================== Flexbox & Grid ====================
    // 容器
    'flexDirection',
    'flexWrap',
    'justifyContent',
    'alignItems',
    'alignContent',
    'justifyItems',

    // 间距
    'gap',      // [加回] 同时设行列
    'rowGap',
    'columnGap',

    // 子元素
    'flex',
    'flexGrow',
    'flexShrink',
    'flexBasis',
    'alignSelf',
    'justifySelf',

    // ==================== 尺寸 (Sizing) ====================
    'width',
    'minWidth',
    'maxWidth',
    'height',
    'minHeight',
    'maxHeight',
    'aspectRatio',
    'boxSizing',

    // ==================== 间距 (Spacing) ====================
    // Margin
    'margin',       // [加回] 极高频
    'marginTop',
    'marginRight',
    'marginBottom',
    'marginLeft',

    // Padding
    'padding',      // [加回] 极高频
    'paddingTop',
    'paddingRight',
    'paddingBottom',
    'paddingLeft',

    // ==================== 排版 (Typography) ====================
    'fontSize',
    'fontWeight',
    'fontStyle',        // [新增] italic, normal
    'fontFamily',       // [新增] 字体族

    'lineHeight',
    'letterSpacing',    // [新增] 字间距
    'textAlign',        // [新增] left, center, right, justify
    'textDecoration',   // [新增] none, underline, line-through
    'textTransform',    // [新增] uppercase, lowercase, capitalize
    'textOverflow',     // [新增] ellipsis, clip
    'whiteSpace',       // [新增] nowrap, pre, pre-wrap
    'wordBreak',        // [新增] break-all, keep-all
    'wordWrap',         // [新增] break-word, normal (overflow-wrap 别名)
    'verticalAlign',    // [新增] top, middle, bottom, baseline
    'color',

    // ==================== 背景 (Background) ====================
    // 注意：background 依旧建议剔除，因为它不是“单值”的逻辑，它太复杂
    'background',
    'backgroundColor',
    'backgroundSize',
    'backgroundRepeat',

    // ==================== 边框 (Border) ====================
    // 简写属性
    'border',       // [新增] 支持 border: none 等
    'borderStyle',  // [新增] 支持 borderStyleNone, borderStyleSolid 等

    // 统一设置 (四个方向) [全部加回]
    'borderWidth',
    'borderColor',
    'borderRadius',

    // 单独方向设置 (拆解)
    'borderTopWidth',
    'borderRightWidth',
    'borderBottomWidth',
    'borderLeftWidth',

    'borderTopColor',
    'borderRightColor',
    'borderBottomColor',
    'borderLeftColor',

    'borderTopLeftRadius',
    'borderTopRightRadius',
    'borderBottomRightRadius',
    'borderBottomLeftRadius',

    // ==================== 视觉效果 (Effects) ====================
    'opacity',
    'boxShadow',
    'outline',          // [新增] none, auto 等
    'outlineStyle',     // [新增] solid, dashed, dotted
    'outlineWidth',     // [新增] 轮廓宽度
    'outlineColor',     // [新增] 轮廓颜色
    'outlineOffset',    // [新增] 轮廓偏移

    // ==================== 交互与其他 (Misc) ====================
    'cursor',
    'userSelect',
    'resize',
    'pointerEvents',    // [新增] none, auto
    'objectFit',        // [新增] cover, contain, fill, none
    'objectPosition',   // [新增] center, top, bottom
];
