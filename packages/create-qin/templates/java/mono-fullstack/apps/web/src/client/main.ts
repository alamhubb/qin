const app = document.getElementById('app')!;

app.innerHTML = `
  <div style="font-family: system-ui; max-width: 600px; margin: 100px auto; text-align: center;">
    <h1>🚀 {{name}}</h1>
    <p>Monorepo 全栈应用</p>
    <p id="api-result">Loading...</p>
  </div>
`;

fetch('/api/hello')
  .then(res => res.json())
  .then(data => {
    document.getElementById('api-result')!.textContent = data.message;
  })
  .catch(() => {
    document.getElementById('api-result')!.textContent = '后端未启动';
  });
