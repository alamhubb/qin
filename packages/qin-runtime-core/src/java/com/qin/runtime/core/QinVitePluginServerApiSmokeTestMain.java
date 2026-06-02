package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies Qin exposes core Vite dev server watcher and websocket APIs to plugins.
 */
public final class QinVitePluginServerApiSmokeTestMain {
    private QinVitePluginServerApiSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-plugin-server-api-");
        Path src = root.resolve("src");
        Files.createDirectories(src);
        Files.writeString(root.resolve("qin.config.js"), """
                {
                  "name": "com.qin.smoke:plugin-server-api",
                  "version": "0.1.0",
                  "frontend": {
                    "srcDir": "src",
                    "outDir": "dist",
                    "devPort": 19097
                  },
                  "dependencies": {
                    "@vitejs/plugin-vue": "^6.0.7",
                    "vite": "^8.0.13",
                    "@vue/compiler-sfc": "^3.5.34",
                    "vue": "^3.5.34"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("vite.config.js"), """
                import vue from '@vitejs/plugin-vue'

                function qinServerApiPlugin() {
                  let serverApiReady = false
                  return {
                    name: 'qin-server-api',
                    configureServer(server) {
                      if (!server || !server.watcher || !server.ws) {
                        this.error('missing Vite server watcher/ws API')
                      }
                      server.watcher.add(['src/Comp.vue', 'src/extra.css'])
                      const watched = server.watcher.getWatched()
                      if (!watched.src || !watched.src.includes('Comp.vue') || !watched.src.includes('extra.css')) {
                        this.error('watcher.getWatched did not include added files')
                      }
                      server.watcher.unwatch('src/extra.css')
                      if (server.watcher.getWatched().src.includes('extra.css')) {
                        this.error('watcher.unwatch did not remove file')
                      }

                      let watcherHit = false
                      const watcherHandler = file => { watcherHit = file === 'src/Comp.vue' }
                      server.watcher.on('change', watcherHandler)
                      server.watcher.emit('change', 'src/Comp.vue')
                      if (!watcherHit) this.error('watcher.on/emit did not call handler')
                      watcherHit = false
                      server.watcher.off('change', watcherHandler)
                      server.watcher.emit('change', 'src/Comp.vue')
                      if (watcherHit) this.error('watcher.off did not remove handler')

                      let wsHit = false
                      const wsHandler = payload => { wsHit = payload && payload.ok === true }
                      server.ws.on('qin:ping', wsHandler)
                      server.ws.emit('qin:ping', { ok: true })
                      if (!wsHit) this.error('ws.on/emit did not call handler')
                      wsHit = false
                      server.ws.off('qin:ping', wsHandler)
                      server.ws.emit('qin:ping', { ok: true })
                      if (wsHit) this.error('ws.off did not remove handler')

                      server.ws.send({ type: 'custom', event: 'qin:server-api', data: { ok: true } })
                      serverApiReady = true
                    },
                    transform(code, id) {
                      if (String(id).includes('Comp.vue') && !String(id).includes('?vue')) {
                        if (!serverApiReady) this.error('configureServer server API checks did not run')
                        return String(code).replace('Server API Works', 'Server API Ready')
                      }
                    }
                  }
                }

                export default {
                  plugins: [qinServerApiPlugin(), vue()]
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("main.js"), """
                import './Comp.vue'
                """, StandardCharsets.UTF_8);
        Files.writeString(src.resolve("Comp.vue"), """
                <template>
                  <section>Server API Works</section>
                </template>
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, src.resolve("main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/src/Comp.vue.js");
        if (mainModule == null || !mainModule.contains("Server API Ready")) {
            throw new IllegalStateException("Expected Vite server API plugin transform, got:\n" + mainModule);
        }

        System.out.println("QinVitePluginServerApiSmokeTestMain OK");
    }
}

