import type { CsstsConfig } from "./types/csstsConfig";
import { atomicCssProperties } from "./CsstsDefaultSupportCssProperties.ts";


// ==================== 系统级别默认配置 ====================

export const csstsDefaultConfig: CsstsConfig = {
    // 默认类名前缀（不含分隔符，内部会自动添加 _）
    classPrefix: 'cssts',

    // 默认支持的属性列表
    properties: atomicCssProperties,

    progressiveRanges: [
        { max: 100, divisors: [1] },         // 0-100: 每个整数
        { max: 200, divisors: [2, 5] },      // 100-200: 能被 2 或 5 整除
        { max: 500, divisors: [5] },         // 200-500: 能被 5 整除
        { max: 1000, divisors: [10] },       // 500-1000: 能被 10 整除
        { max: 2000, divisors: [20, 50] },   // 1000-2000: 能被 20 或 50 整除
        { max: 5000, divisors: [50] },       // 2000-5000: 能被 50 整除
        { max: 10000, divisors: [100] },     // 5000-10000: 能被 100 整除
        { max: Infinity, divisors: [1000] }, // 10000+: 能被 1000 整除
    ],

    pseudoClassConfig: {
        hover: { filter: 'brightness(1.15)' },
        active: { filter: 'brightness(0.85)' },
        focus: {
            outline: '2px solid var(--el-color-primary-light-5, #79bbff)',
            outlineOffset: '1px'
        },
        disabled: { opacity: '0.5', cursor: 'not-allowed', filter: 'grayscale(0.2)' }
    },

    groups: [
        { name: 'size', numberProperties: ['height', 'width'] },
        { name: 'marginX', numberProperties: ['marginLeft', 'marginRight'] },
        { name: 'marginY', numberProperties: ['marginTop', 'marginBottom'] },
        { name: 'paddingX', numberProperties: ['paddingLeft', 'paddingRight'] },
        { name: 'paddingY', numberProperties: ['paddingTop', 'paddingBottom'] },
        {
            name: 'flexRow', keywordProperties: { display: 'flex', flexDirection: 'row' },
        },
        {
            name: 'flexCol', keywordProperties: { display: 'flex', flexDirection: 'column' },
        },
        // row + flex
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'row' }],
                flex: [0, 1, 'auto', 'none'],
            }
        },
        // col + flex
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'column' }],
                flex: [0, 1, 'auto', 'none'],
            }
        },
        // row + wrap
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'row' }],
                flexWrap: ['nowrap', 'wrap'],
            }
        },
        // col + wrap
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'column' }],
                flexWrap: ['nowrap', 'wrap'],
            }
        },
        // row + justifyContent (x轴)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'row' }],
                justifyContent: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'space-between' },
                    { value: 'space-evenly' },
                    { value: 'space-around' },
                ],
            }
        },
        // col + justifyContent (y轴)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'column' }],
                justifyContent: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'space-between' },
                    { value: 'space-evenly' },
                    { value: 'space-around' },
                ],
            }
        },
        // row + alignItems (y轴)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'row' }],
                alignItems: {
                    prefix: 'y',
                    values: [
                        { value: 'start' },
                        { value: 'center' },
                        { value: 'end' },
                        { value: 'stretch' },
                        { value: 'baseline' },
                    ]
                },
            }
        },
        // col + alignItems (x轴)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'column' }],
                alignItems: {
                    prefix: 'x',
                    values: [
                        { value: 'start' },
                        { value: 'center' },
                        { value: 'end' },
                        { value: 'stretch' },
                        { value: 'baseline' },
                    ]
                }
            }
        },
        // row + justifyContent(x) + alignItems(y)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'row' }],
                justifyContent: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'space-between' },
                    { value: 'space-evenly' },
                    { value: 'space-around' },
                ],
                alignItems: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'stretch' },
                    { value: 'baseline' },
                ],
            }
        },
        // col + justifyContent(y) + alignItems(x)
        {
            keywordIterations: {
                display: [{ value: 'flex' }],
                flexDirection: [{ value: 'column' }],
                justifyContent: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'space-between' },
                    { value: 'space-evenly' },
                    { value: 'space-around' },
                ],
                alignItems: [
                    { value: 'start' },
                    { value: 'center' },
                    { value: 'end' },
                    { value: 'stretch' },
                    { value: 'baseline' },
                ],
            }
        },
    ],

    colors: [
        'transparent',
        'black',
        'white',
        'red',
        'green',
        'blue',
        'yellow',
        'gray',
        'grey',
        'cyan',
        'magenta',
        'orange',
        'silver',
        'purple',
        'pink',
        'brown',
        'lime',
        'navy',
        'teal',
        'olive',
        'skyblue',
        'lightgray',
        'darkgray',
        'gold',
        'salmon',
        'tomato',
        'violet'
    ],

    excludeKeywords: [
        'a98Rgb',
        'decreasing',
        'displayP3',
        'hsl',
        'hue',
        'hwb',
        'in',
        'increasing',
        'lab',
        'lch',
        'longer',
        'oklab',
        'oklch',
        'prophotoRgb',
        'rec2020',
        'shorter',
        'srgb',
        'srgbLinear',
        'xyz',
        'xyzD50',
        'xyzD65',
        'initial',
        'unset',
        'revert',
        'revertLayer',
    ],

    // 排除不常用的数值类别
    excludeNumberCategories: [
        'physical',    // cm, mm, in, pt, pc - 仅用于打印
        'frequency',   // Hz, kHz - 音频相关，Web 几乎不用
        'resolution',  // dpi, dpcm, dppx - 主要用于媒体查询
    ],

    // 数值类别详细配置
    numberCategoriesConfig: [
        {
            // 像素单位 - 最常用，支持负值
            pixel: {
                min: 0,
                max: 1000,
            },
        },
        {
            // 字体相对单位 - 只使用 em 和 rem
            fontRelative: {
                min: 0,
                max: 20,
                step: 1,
                units: ['em', 'rem'],  // 只生成 em 和 rem，排除 ch, ex, cap, ic, lh, rlh
            },
        },
        {
            // 百分比类 (%, vw, vh, vmin, vmax, etc.)
            percentage: {
                min: 0,
                max: 100,
                step: 1,
                presets: [33.33, 66.67],
                units: ['percent', 'vw', 'vh'],
            },
        },
        {
            // 角度单位 (deg, rad, turn, grad)
            angle: {
                min: -360,
                max: 360,
                step: [10, 15]
            },
        },
        {
            // 时间单位 (s, ms)
            time: {
                min: 0,
                max: 5
            },
        },
        {
            // 无单位数值 - opacity, z-index, line-height, flex-grow 等
            unitless: {
                min: -20,
                max: 20,
                step: 1,
            },
        },
        {
            // Flex 单位 (fr) - Grid 布局常用
            flex: {
                min: 0,
                max: 12,
                step: 1
            },
        }
    ],

    // ==================== 特殊属性配置 ====================
    propertiesConfig: [
        {
            height: {
                px: {
                    min: 0,
                    max: 10000
                }
            }
        },
        {
            width: {
                px: {
                    min: 0,
                    max: 10000
                }
            }
        },
        {
            margin: {
                px: {
                    min: -10000,
                    max: 10000
                }
            }
        },
        {
            padding: {
                px: {
                    min: 0,
                    max: 10000
                }
            }
        },
        // z-index: 通常使用特定层级
        {
            zIndex: {
                unitless: {
                    min: -1,
                    max: 10000,
                }
            }
        },

        // opacity: 0-1 范围，步长 0.1
        {
            opacity: {
                unitless: {
                    min: 0,
                    max: 1,
                    step: 0.1,
                }
            }
        },

        // line-height: 无单位时通常 1-3
        {
            lineHeight: {
                unitless: {
                    min: 0,
                    max: 3,
                    step: 0.25,
                }
            }
        },

        // font-weight: 100-900，步长 100
        {
            fontWeight: {
                unitless: {
                    min: 100,
                    max: 900,
                    step: 100,
                }
            }
        },

        // border-radius: 通常 0-50px 或百分比
        {
            borderRadius: {
                pixel: {
                    min: 0,
                    max: 100,
                    step: 1
                },
                percentage: {
                    min: 0,
                    max: 50,
                    step: 5
                }
            }
        },

        // scale: 通常 0-2 范围
        {
            scale: {
                unitless: {
                    min: 0,
                    max: 2,
                    step: 0.1
                }
            }
        },

        // aspect-ratio: 常见比例
        {
            aspectRatio: {
                unitless: {
                    min: 0,
                    max: 3,
                    step: 0.1,
                }
            }
        },
    ],

    // 类组合配置
    classGroup: {
        click: ['hover', 'active', 'focus', 'disabled', 'cursorPointer'],
        ddClick: ['click', 'colorRed'],  // 引用 click 组合 + colorRed
    },
};
