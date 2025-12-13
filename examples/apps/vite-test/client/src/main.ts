console.log("Vite frontend loaded!");

document.querySelector("#app")!.innerHTML += `
  <p>TypeScript is working! Time: ${new Date().toLocaleTimeString()}</p>
`;
