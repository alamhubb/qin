import type { CsstsConfig } from './types/csstsConfig';
import type {
    CssPropertyName,
    CssNumberCategoryName,
    CssColorName,
    CssProgressiveRange,
    GroupConfig,
    CssStepConfig,
} from './types/cssPropertyConfig';
import { atomicCssProperties } from './CsstsDefaultSupportCssProperties.ts';

export class ConfigLookup {
    static init(userConfig?: CsstsConfig): void {
    }

    static reset(): void {
    }

    static get properties(): CssPropertyName[] | undefined {
        return atomicCssProperties;
    }

    static get excludeProperties(): CssPropertyName[] | undefined {
        return undefined;
    }

    static get colors(): CssColorName[] | undefined {
        return [
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
            'violet',
        ];
    }

    static get excludeColors(): CssColorName[] | undefined {
        return undefined;
    }

    static get progressiveRanges(): CssProgressiveRange[] | undefined {
        return undefined;
    }

    static get groups(): GroupConfig[] | undefined {
        return undefined;
    }

    static get numberCategories(): CssNumberCategoryName[] | undefined {
        return undefined;
    }

    static get excludeNumberCategories(): CssNumberCategoryName[] | undefined {
        return ['physical', 'frequency', 'resolution'];
    }

    static get classPrefix(): string {
        return 'cssts_';
    }

    static get excludeKeywords() {
        return [
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
        ];
    }

    static get colorTypes() {
        return undefined;
    }

    static get excludeColorTypes() {
        return undefined;
    }

    static get pseudoClassConfig() {
        return undefined;
    }

    static get classGroup(): Record<string, string[]> | undefined {
        return undefined;
    }

    static getCategoryConfig(categoryName: string): CssStepConfig | undefined {
        return undefined;
    }

    static getPropertyConfig(propertyName: string): Record<string, CssStepConfig> | undefined {
        return undefined;
    }
}
