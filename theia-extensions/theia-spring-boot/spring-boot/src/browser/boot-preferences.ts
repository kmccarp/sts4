import { interfaces } from 'inversify';
import { createPreferenceProxy, PreferenceProxy, PreferenceService, PreferenceContribution, PreferenceSchema } from '@theia/core/lib/browser';

// tslint:disable:max-line-length

export const HIGHLIGHTS_PREF_NAME = 'boot-java.boot-hints.on';
export const CODELENS_PREF_NAME = 'boot-java.highlight-codelens.on';

export const BootConfigSchema: PreferenceSchema = {
    'type': 'object',
    'title': 'Spring Boot Java Configuration',
    properties: {
        'boot-java.boot-hints.on': {
            type: 'boolean',
            description: 'Enable/Disable Spring running Boot application live hints decorators in Java source code.',
            default: true
        },
        'boot-java.change-detection.on': {
            type: 'boolean',
            description: 'Enable/Disable detecting changes of running Spring Boot applications.',
            default: false
        },
        'boot-java.highlight-codelens.on': {
            type: 'boolean',
            default: true,
            description: 'Enable/Disable Spring running Boot application Code Lenses'
        }
    }
};

export interface BootConfiguration {
    'boot-java.boot-hints.on': boolean;
    'boot-java.change-detection.on': boolean;
    'boot-java.highlight-codelens.on': boolean;
}

export const BootPreferences = Symbol('BootPreferences');
export type BootPreferences = PreferenceProxy<BootConfiguration>;

export function createBootPreferences(preferences: PreferenceService): BootPreferences {
    return createPreferenceProxy(preferences, BootConfigSchema);
}

export function bindBootPreferences(bind: interfaces.Bind): void {
    bind(BootPreferences).toDynamicValue(ctx => {
        const preferences = ctx.container.get<PreferenceService>(PreferenceService);
        return createBootPreferences(preferences);
    });
    bind(PreferenceContribution).toConstantValue({ schema: BootConfigSchema });
}
