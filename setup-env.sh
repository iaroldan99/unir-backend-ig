#!/bin/bash

# Script interactivo para configurar credenciales de Instagram
# Equipo 3 - Instagram Integration

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}   Configuración de Credenciales de Instagram API${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

# Paso 1: Access Token
echo -e "${YELLOW}Paso 1: Access Token${NC}"
echo "Por favor, pega tu Access Token de Instagram (el que generaste en Meta):"
read -r ACCESS_TOKEN

if [ -z "$ACCESS_TOKEN" ]; then
    echo -e "${RED}Error: Access Token es requerido${NC}"
    exit 1
fi

# Paso 2: Verificar el token y obtener información
echo -e "\n${YELLOW}Verificando token y obteniendo información...${NC}"

# Obtener las páginas de Facebook asociadas
echo -e "${BLUE}Obteniendo tus páginas de Facebook...${NC}"
PAGES_RESPONSE=$(curl -s "https://graph.facebook.com/v18.0/me/accounts?access_token=${ACCESS_TOKEN}")

echo "$PAGES_RESPONSE" | jq '.' 2>/dev/null || echo "$PAGES_RESPONSE"

# Extraer el primer Page ID
PAGE_ID=$(echo "$PAGES_RESPONSE" | jq -r '.data[0].id' 2>/dev/null)
PAGE_NAME=$(echo "$PAGES_RESPONSE" | jq -r '.data[0].name' 2>/dev/null)

if [ "$PAGE_ID" = "null" ] || [ -z "$PAGE_ID" ]; then
    echo -e "${RED}No se pudo obtener el Page ID automáticamente.${NC}"
    echo "Por favor, ingresa tu Facebook Page ID manualmente:"
    read -r PAGE_ID
fi

echo -e "${GREEN}✓ Page ID: $PAGE_ID ($PAGE_NAME)${NC}"

# Obtener Instagram Business Account ID
echo -e "\n${BLUE}Obteniendo tu Instagram Business Account ID...${NC}"
IG_RESPONSE=$(curl -s "https://graph.facebook.com/v18.0/${PAGE_ID}?fields=instagram_business_account&access_token=${ACCESS_TOKEN}")

echo "$IG_RESPONSE" | jq '.' 2>/dev/null || echo "$IG_RESPONSE"

INSTAGRAM_PAGE_ID=$(echo "$IG_RESPONSE" | jq -r '.instagram_business_account.id' 2>/dev/null)

if [ "$INSTAGRAM_PAGE_ID" = "null" ] || [ -z "$INSTAGRAM_PAGE_ID" ]; then
    echo -e "${RED}No se pudo obtener el Instagram Page ID automáticamente.${NC}"
    echo "Por favor, ingresa tu Instagram Business Account ID manualmente:"
    read -r INSTAGRAM_PAGE_ID
fi

echo -e "${GREEN}✓ Instagram Page ID: $INSTAGRAM_PAGE_ID${NC}"

# Paso 3: Verify Token (ya generado)
VERIFY_TOKEN="instagram_verify_01fc042c51fc71fe716df639c7e48f99"
echo -e "\n${GREEN}✓ Verify Token: $VERIFY_TOKEN${NC}"

# Paso 4: Crear archivo .env
echo -e "\n${YELLOW}Paso 4: Creando archivo .env...${NC}"

cat > .env << EOF
# Instagram API Configuration
# Generado automáticamente por setup-env.sh

INSTAGRAM_ACCESS_TOKEN=${ACCESS_TOKEN}
INSTAGRAM_VERIFY_TOKEN=${VERIFY_TOKEN}
INSTAGRAM_PAGE_ID=${INSTAGRAM_PAGE_ID}
SERVER_PORT=8080
EOF

echo -e "${GREEN}✓ Archivo .env creado correctamente${NC}"

# Paso 5: Mostrar resumen
echo -e "\n${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Configuración completada${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

echo "Credenciales configuradas:"
echo "• Access Token: ${ACCESS_TOKEN:0:20}..."
echo "• Verify Token: $VERIFY_TOKEN"
echo "• Instagram Page ID: $INSTAGRAM_PAGE_ID"
echo ""

echo -e "${YELLOW}Próximos pasos:${NC}"
echo "1. Cargar las variables de entorno:"
echo "   ${GREEN}export \$(cat .env | xargs)${NC}"
echo ""
echo "2. Reiniciar la aplicación con las nuevas credenciales"
echo ""
echo "3. Probar el envío de mensajes:"
echo "   ${GREEN}curl -X POST http://localhost:8080/api/messages/send \\${NC}"
echo "     ${GREEN}-H 'Content-Type: application/json' \\${NC}"
echo "     ${GREEN}-d '{\"recipientId\": \"USER_IG_ID\", \"text\": \"Hola!\"}'${NC}"
echo ""

echo -e "${YELLOW}Nota:${NC} Para recibir mensajes, necesitas configurar el webhook con ngrok."
echo ""

