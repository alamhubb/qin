/**
 * DTS 生成脚本入口
 *
 * 运行: tsx src/dts/dts-cli.ts
 */

import * as path from 'path';
import { generateDtsFiles } from './dts-writer.ts';

const __dirname = path.resolve('.');

// 本地测试：输出到 target/cssts-dts 目录
const OUTPUT_DIR = path.resolve(__dirname, '../../target/cssts-dts');

console.log('🚀 开始生成 .d.ts 文件...\n');

const result = generateDtsFiles({
  dtsOutputDir: OUTPUT_DIR,
  dtsSplitFiles: true,
  debug: true,
});

console.log(`\n📋 生成了 ${result.files.length} 个文件`);
console.log(`   总原子类数: ${result.stats.totalAtoms}`);
console.log(`   属性数: ${result.stats.propertyCount}`);
