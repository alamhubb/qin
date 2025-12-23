import './style.css'

document.querySelector('#app').innerHTML = `
  <div class="container">
    <h1>🚀 Vite + Qin</h1>
    <p>这个项目由 Qin 管理，不使用 npm！</p>
    <p>包从 npm 镜像获取，存储在本地。</p>
    <div class="card">
      <button id="counter" type="button">点击计数: 0</button>
    </div>
  </div>
`

let count = 0
document.querySelector('#counter').addEventListener('click', (e) => {
  count++
  e.target.textContent = `点击计数: ${count}`
})

console.log('Vite project managed by Qin!')
