# 📚 Índice de Documentación - Instagram Messaging API

## 🚀 Inicio Rápido (Empieza aquí)

1. **[QUICK_START.md](QUICK_START.md)** ⚡  
   Guía para tener el sistema funcionando en menos de 10 minutos

2. **[PASOS_CONFIGURACION.md](PASOS_CONFIGURACION.md)** 📋  
   Checklist visual de configuración paso a paso

3. **[../scripts/test-api.sh](../scripts/test-api.sh)** 🧪  
   Script para verificar que la API funciona
   ```bash
   ./scripts/test-api.sh
   ```

---

## 📖 Documentación Completa

### Configuración

- **[SETUP_GUIDE.md](SETUP_GUIDE.md)** 🔧  
  Guía detallada de configuración completa desde cero

- **[CREDENCIALES_INSTAGRAM.md](CREDENCIALES_INSTAGRAM.md)** 🔐  
  Cómo obtener credenciales de Meta Developer Portal

- **[../scripts/setup-credentials.sh](../scripts/setup-credentials.sh)** 🤖  
  Asistente interactivo para configurar credenciales
  ```bash
  ./scripts/setup-credentials.sh
  ```

### Documentación General

- **[../README.md](../README.md)** 📘  
  Documentación general del proyecto, arquitectura y uso

- **[GIT_CONFIG.md](GIT_CONFIG.md)** 🔧  
  Configuración de Git para este repositorio

### Testing y Ejemplos

- **[../scripts/curl-examples.sh](../scripts/curl-examples.sh)** 💻  
  Ejemplos detallados de prueba con cURL
  ```bash
  ./scripts/curl-examples.sh
  ```

- **[test-api.sh](test-api.sh)** ✅  
  Suite de tests automatizados

### Plantillas

- **[../env-template.txt](../env-template.txt)** 📝  
  Plantilla para variables de entorno

### Colección de Postman

- **[../postman/Instagram_API.postman_collection.json](../postman/Instagram_API.postman_collection.json)** 📮  
  Colección completa de endpoints para importar en Postman

---

## 🎯 Flujos de Trabajo

### Primer Uso (Setup Inicial)

```
1. QUICK_START.md
   ↓
2. setup-credentials.sh
   ↓
3. test-api.sh
   ↓
4. ¡Listo para usar!
```

### Configuración Detallada

```
1. SETUP_GUIDE.md
   ↓
2. CREDENCIALES_INSTAGRAM.md
   ↓
3. Configurar webhook con ngrok
   ↓
4. curl-examples.sh
```

### Solo Testing

```
1. mvn spring-boot:run
   ↓
2. ./test-api.sh
```

---

## 📁 Estructura del Proyecto

```
Seminario/
├── 📚 DOCUMENTACIÓN (docs/)
│   ├── INDEX.md                          ← Estás aquí
│   ├── QUICK_START.md                    ← Inicio rápido
│   ├── SETUP_GUIDE.md
│   ├── PASOS_CONFIGURACION.md
│   ├── CREDENCIALES_INSTAGRAM.md
│   ├── CURL_COMMANDS.md
│   ├── GUIA_RAPIDA_IG.md
│   └── GIT_CONFIG.md
│
├── 🔧 SCRIPTS (scripts/)
│   ├── setup-credentials.sh              ← Configurar credenciales
│   ├── test-api.sh                       ← Tests automatizados
│   └── curl-examples.sh                  ← Ejemplos de prueba
│
├── 📮 POSTMAN (postman/)
│   └── Instagram_API.postman_collection.json
│
├── 📝 CONFIGURACIÓN
│   ├── README.md                         ← Documentación principal
│   ├── env-template.txt                  ← Template de .env
│   ├── .gitignore                        ← Git ignore
│   └── pom.xml                           ← Maven config
│
├── 💻 CÓDIGO FUENTE
│   ├── src/main/java/
│   │   └── com/uade/seminario/
│   │       ├── InstagramMessagingApplication.java
│   │       ├── config/
│   │       │   ├── InstagramConfig.java
│   │       │   └── WebClientConfig.java
│   │       ├── controller/
│   │       │   ├── WebhookController.java
│   │       │   ├── MessageController.java
│   │       │   └── UnifiedMessageController.java
│   │       ├── service/
│   │       │   ├── InstagramMessageSenderService.java
│   │       │   └── MessageProcessorService.java
│   │       └── model/
│   │           ├── IncomingMessage.java
│   │           ├── OutgoingMessage.java
│   │           └── MessageDTO.java
│   └── src/main/resources/
│       └── application.yml
│
└── 🔨 BUILD
    └── pom.xml                           ← Maven config
```

---

## 🎓 Guías por Nivel

### Principiante

1. Lee: **QUICK_START.md**
2. Ejecuta: `./test-api.sh`
3. Consulta: **README.md**

### Intermedio

1. Lee: **SETUP_GUIDE.md**
2. Configura: `./setup-credentials.sh`
3. Prueba: `./curl-examples.sh`

### Avanzado

1. Lee: **CREDENCIALES_INSTAGRAM.md**
2. Revisa el código en `src/`
3. Personaliza según necesidades

---

## 🔗 Links Externos Útiles

### Meta / Instagram

- [Meta for Developers](https://developers.facebook.com/)
- [Graph API Explorer](https://developers.facebook.com/tools/explorer/)
- [Instagram Messaging Docs](https://developers.facebook.com/docs/messenger-platform/instagram)
- [Webhook Testing Tool](https://developers.facebook.com/tools/webhooks/)

### Herramientas

- [ngrok](https://ngrok.com/) - Túneles HTTPS para desarrollo local
- [Postman](https://www.postman.com/) - Testing de APIs
- [JSONLint](https://jsonlint.com/) - Validador de JSON

### Spring Boot

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)

---

## 🆘 Soporte

### Problemas Comunes

Consulta la sección **"🐛 Solución de Problemas"** en:
- `SETUP_GUIDE.md`
- `CREDENCIALES_INSTAGRAM.md`

### Debug

```bash
# Ver logs de la aplicación
mvn spring-boot:run

# Ver logs en tiempo real
tail -f logs/spring-boot-application.log

# Verificar variables de entorno
echo $INSTAGRAM_ACCESS_TOKEN
```

---

## ✅ Checklist Final

Antes de ir a producción:

- [ ] Credenciales configuradas en `.env`
- [ ] Access Token de larga duración
- [ ] Webhook verificado en Meta
- [ ] Tests pasando (`./test-api.sh`)
- [ ] Mensaje de prueba enviado
- [ ] Mensaje de prueba recibido
- [ ] Logs funcionando correctamente
- [ ] `.env` en `.gitignore`
- [ ] Documentación leída
- [ ] URLs de producción configuradas

---

## 🎉 Siguientes Pasos

Una vez configurado:

1. **Integrar con sistema unificado**
   - Conectar con WhatsApp API
   - Conectar con Gmail API
   - Crear capa de abstracción común

2. **Agregar persistencia**
   - Base de datos para mensajes
   - Historial de conversaciones

3. **Implementar cola de mensajes**
   - RabbitMQ o Kafka
   - Procesamiento asíncrono

4. **Deploy a producción**
   - Configurar servidor HTTPS
   - Variables de entorno en servidor
   - Monitoreo y logs

---

**Equipo 3 - Instagram Integration**  
**UADE Seminario 2025**

Para empezar, ejecuta:
```bash
cat QUICK_START.md
```

O si prefieres configuración interactiva:
```bash
./setup-credentials.sh
```

