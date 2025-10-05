#!/bin/bash

# 🔧 Script con ejemplos de cURL para probar la API de Instagram Messaging
# Equipo 3 - Instagram Integration

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuración
BASE_URL="http://localhost:8080"

echo -e "${BLUE}════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}   Instagram Messaging API - Ejemplos de Prueba${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════════${NC}\n"

# Función para imprimir headers de sección
print_section() {
    echo -e "\n${YELLOW}▶ $1${NC}"
    echo -e "${YELLOW}────────────────────────────────────────────────────${NC}"
}

# Función para ejecutar y mostrar comandos
run_command() {
    echo -e "${GREEN}$ $1${NC}"
    eval $1
    echo ""
}

# ═══════════════════════════════════════════════════════
# 1. HEALTH CHECKS
# ═══════════════════════════════════════════════════════

print_section "1. Health Checks"

echo "Verificando que la API esté funcionando..."
run_command "curl -s ${BASE_URL}/webhook/health"

echo "Verificando el controlador de mensajes..."
run_command "curl -s ${BASE_URL}/api/messages/health | jq ."

# ═══════════════════════════════════════════════════════
# 2. VERIFICACIÓN DE WEBHOOK
# ═══════════════════════════════════════════════════════

print_section "2. Verificación de Webhook (Simulación)"

echo "Simulando la verificación que hace Meta..."
VERIFY_TOKEN="your-verify-token-here"  # Reemplaza con tu token
run_command "curl -s '${BASE_URL}/webhook?hub.mode=subscribe&hub.verify_token=${VERIFY_TOKEN}&hub.challenge=test12345'"

# ═══════════════════════════════════════════════════════
# 3. RECEPCIÓN DE MENSAJES (Simulación)
# ═══════════════════════════════════════════════════════

print_section "3. Simular Recepción de Mensaje de Instagram"

echo "Enviando un webhook simulado (mensaje de texto)..."
run_command "curl -X POST ${BASE_URL}/webhook \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"object\": \"instagram\",
    \"entry\": [
      {
        \"id\": \"instagram-page-id-123\",
        \"time\": 1694564800,
        \"messaging\": [
          {
            \"sender\": {
              \"id\": \"sender-instagram-user-id-456\"
            },
            \"recipient\": {
              \"id\": \"your-instagram-business-account-id\"
            },
            \"timestamp\": 1694564800000,
            \"message\": {
              \"mid\": \"message-id-789\",
              \"text\": \"Hola! Este es un mensaje de prueba desde Instagram\"
            }
          }
        ]
      }
    ]
  }'"

echo -e "${YELLOW}Nota: Revisa los logs de tu aplicación para ver el mensaje procesado.${NC}"

# ═══════════════════════════════════════════════════════
# 4. ENVÍO DE MENSAJES
# ═══════════════════════════════════════════════════════

print_section "4. Enviar Mensaje de Texto a Instagram"

echo "⚠️  IMPORTANTE: Reemplaza 'INSTAGRAM_USER_ID' con un ID real"
echo "El usuario debe haberte enviado un mensaje primero (requisito de Instagram)"
echo ""

RECIPIENT_ID="INSTAGRAM_USER_ID"  # Reemplaza con un ID real

run_command "curl -X POST ${BASE_URL}/api/messages/send \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"recipientId\": \"${RECIPIENT_ID}\",
    \"text\": \"¡Hola! Este es un mensaje enviado desde nuestra API de mensajería unificada. 🚀\"
  }' | jq ."

# ═══════════════════════════════════════════════════════
# 5. EJEMPLOS ADICIONALES
# ═══════════════════════════════════════════════════════

print_section "5. Ejemplos Adicionales"

echo "Ejemplo: Enviar mensaje con emojis..."
run_command "curl -X POST ${BASE_URL}/api/messages/send \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"recipientId\": \"${RECIPIENT_ID}\",
    \"text\": \"¡Hola! 👋 ¿Cómo estás? 😊\"
  }' | jq ."

echo "Ejemplo: Enviar mensaje largo..."
run_command "curl -X POST ${BASE_URL}/api/messages/send \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"recipientId\": \"${RECIPIENT_ID}\",
    \"text\": \"Este es un mensaje más largo para probar cómo se manejan los textos extensos. Nuestra API de mensajería unificada permite integrar Instagram, WhatsApp y Gmail en una sola plataforma.\"
  }' | jq ."

# ═══════════════════════════════════════════════════════
# 6. TESTING CON MENSAJES CON ADJUNTOS (Simulación)
# ═══════════════════════════════════════════════════════

print_section "6. Simular Mensaje con Imagen"

echo "Simulando webhook de mensaje con imagen adjunta..."
run_command "curl -X POST ${BASE_URL}/webhook \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"object\": \"instagram\",
    \"entry\": [
      {
        \"id\": \"instagram-page-id-123\",
        \"time\": 1694564900,
        \"messaging\": [
          {
            \"sender\": {
              \"id\": \"sender-instagram-user-id-456\"
            },
            \"recipient\": {
              \"id\": \"your-instagram-business-account-id\"
            },
            \"timestamp\": 1694564900000,
            \"message\": {
              \"mid\": \"message-id-image-001\",
              \"text\": \"Mira esta imagen\",
              \"attachments\": [
                {
                  \"type\": \"image\",
                  \"payload\": {
                    \"url\": \"https://example.com/image.jpg\"
                  }
                }
              ]
            }
          }
        ]
      }
    ]
  }'"

# ═══════════════════════════════════════════════════════
# 7. MANEJO DE ERRORES
# ═══════════════════════════════════════════════════════

print_section "7. Pruebas de Manejo de Errores"

echo "Test: Enviar mensaje sin recipientId (debe fallar)..."
run_command "curl -X POST ${BASE_URL}/api/messages/send \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"text\": \"Mensaje sin destinatario\"
  }' | jq ."

echo "Test: Enviar mensaje sin texto (debe fallar)..."
run_command "curl -X POST ${BASE_URL}/api/messages/send \\
  -H 'Content-Type: application/json' \\
  -d '{
    \"recipientId\": \"123456789\"
  }' | jq ."

# ═══════════════════════════════════════════════════════
# FINALIZACIÓN
# ═══════════════════════════════════════════════════════

echo -e "\n${BLUE}════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Tests completados${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════════${NC}\n"

echo -e "${YELLOW}Notas:${NC}"
echo "• Reemplaza 'INSTAGRAM_USER_ID' con un ID real de usuario"
echo "• El usuario debe enviarte un mensaje primero antes de que puedas responder"
echo "• Revisa los logs de la aplicación para ver los mensajes procesados"
echo "• Usa 'jq' para formatear JSON (instalar: brew install jq)"
echo ""
echo -e "${YELLOW}Para más información, consulta:${NC}"
echo "• README.md - Documentación general"
echo "• SETUP_GUIDE.md - Guía de configuración paso a paso"
echo ""


