/**
 * Qin 全栈示例 - 前端 JavaScript
 * 使用原生 fetch API 调用 Java 后端
 */

// API 基础地址（开发时可能需要代理）
const API_BASE = '';

// Toast 提示
function showToast(message) {
  const toast = document.getElementById('toast');
  toast.textContent = message;
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3000);
}

// 显示结果
function showResult(elementId, data) {
  const el = document.getElementById(elementId);
  el.textContent = JSON.stringify(data, null, 2);
  el.classList.add('show');
}

// 获取服务器时间
async function getServerTime() {
  try {
    const response = await fetch(`${API_BASE}/api/time`);
    const data = await response.json();
    showResult('timeResult', data);
    showToast('✅ 获取时间成功');
  } catch (error) {
    showToast('❌ 请求失败: ' + error.message);
  }
}

// 发送问候
async function sendGreet() {
  const name = document.getElementById('nameInput').value || '世界';
  try {
    const response = await fetch(`${API_BASE}/api/greet`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    const data = await response.json();
    showResult('greetResult', data);
    showToast('✅ 问候发送成功');
  } catch (error) {
    showToast('❌ 请求失败: ' + error.message);
  }
}

// 获取用户列表
async function getUsers() {
  try {
    const response = await fetch(`${API_BASE}/api/users`);
    const users = await response.json();
    
    const list = document.getElementById('usersList');
    list.innerHTML = users.map(user => `
      <li>
        <span class="name">${user.name}</span>
        <span class="role">${user.role}</span>
      </li>
    `).join('');
    
    showToast(`✅ 获取到 ${users.length} 个用户`);
  } catch (error) {
    showToast('❌ 请求失败: ' + error.message);
  }
}

// 绑定事件
document.getElementById('getTimeBtn').addEventListener('click', getServerTime);
document.getElementById('greetBtn').addEventListener('click', sendGreet);
document.getElementById('getUsersBtn').addEventListener('click', getUsers);

// 回车发送问候
document.getElementById('nameInput').addEventListener('keypress', (e) => {
  if (e.key === 'Enter') sendGreet();
});

// 页面加载完成
console.log('🚀 Qin 前端已加载');
