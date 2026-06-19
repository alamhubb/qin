/**
 * DTS 生成模块
 *
 * 提供原子类生成和 DTS 文件写入功能
 */

export {
  generateAtoms,
  generateDts,
  generateStats,
  generateAtomsByProperty,
  generatePropertyDts,
  generateIndexDts,
  type AtomDefinition,
  type AtomsByProperty,
} from './atom-generator.ts';

export {
  generateDtsFiles,
  type DtsGenerateResult,
} from './dts-writer.ts';
