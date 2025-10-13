#!/bin/bash

# Script para verificar vinculación de Instagram Business
# Equipo 3 - Instagram Integration

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

ACCESS_TOKEN="EAALj0YtG29cBPojd5RdcwkZCBsI6j5GeYvamRifNaZBum8GCY1xMgh5krl2E9jRxlDuZBKdSXuwKCtOnUwuCLYF796RLnXIUyGoZAUATOp0oHFOUgvAH5HKbQ2vuGhDPSdRGR5jDxjZAHRZCZA4BZBSXwTI6qcHj7NPAZCkY74hjLbWTv6Ub5fedHZAZCaLkLnnMskVUdscj1kWHWNYjBMdrpawoHbfQ9XkxSow51uMOlKC1JaWiGmOQn2stgZDZD"

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}   Verificación de Instagram Business Vinculado${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

echo -e "${YELLOW}Buscando páginas de Facebook con Instagram vinculado...${NC}\n"

# Obtener todas las páginas automáticamente
PAGES_JSON=$(curl -s "https://graph.facebook.com/v18.0/me/accounts?access_token=${ACCESS_TOKEN}")
PAGES=$(echo "$PAGES_JSON" | python3 -c "import sys, json; data = json.load(sys.stdin); [print(f\"{p['id']}:{p['name']}\") for p in data.get('data', [])]" 2>/dev/null)

FOUND=0

while IFS=':' read -r page_id page_name; do
  if [ -z "$page_id" ]; then
    continue
  fi
  
  # Obtener info de Instagram
  RESPONSE=$(curl -s "https://graph.facebook.com/v18.0/${page_id}?fields=name,instagram_business_account&access_token=${ACCESS_TOKEN}")
  
  IG_ID=$(echo "$RESPONSE" | python3 -c "import sys, json; data = json.load(sys.stdin); print(data.get('instagram_business_account', {}).get('id', 'NONE'))" 2>/dev/null)
  
  if [ "$IG_ID" != "NONE" ] && [ ! -z "$IG_ID" ]; then
    echo -e "${GREEN}✓ ${page_name}${NC}"
    echo -e "  ${BLUE}Page ID:${NC} ${page_id}"
    echo -e "  ${GREEN}Instagram ID:${NC} ${IG_ID}"
    
    # Obtener info adicional de Instagram
    IG_INFO=$(curl -s "https://graph.facebook.com/v18.0/${IG_ID}?fields=username,name&access_token=${ACCESS_TOKEN}")
    IG_USERNAME=$(echo "$IG_INFO" | python3 -c "import sys, json; data = json.load(sys.stdin); print(data.get('username', 'N/A'))" 2>/dev/null)
    
    echo -e "  ${BLUE}Instagram Username:${NC} @${IG_USERNAME}"
    echo ""
    
    FOUND=$((FOUND + 1))
    
    # Guardar para actualizar .env
    LAST_IG_ID=$IG_ID
    LAST_PAGE_NAME=$page_name
  fi
done <<< "$PAGES"

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

if [ $FOUND -eq 0 ]; then
  echo -e "${RED}✗ No se encontraron cuentas de Instagram vinculadas${NC}\n"
  echo -e "${YELLOW}Pasos a seguir:${NC}"
  echo "1. Abre Instagram en tu móvil"
  echo "2. Convierte tu cuenta a Instagram Business"
  echo "3. Vincula con una de tus páginas de Facebook"
  echo "4. Vuelve a ejecutar este script"
  echo ""
  echo -e "${BLUE}Ver guía completa en: SIGUIENTE_PASO_INSTAGRAM.md${NC}"
else
  echo -e "${GREEN}✓ Se encontraron $FOUND cuenta(s) de Instagram vinculada(s)${NC}\n"
  
  if [ $FOUND -eq 1 ]; then
    echo -e "${YELLOW}¿Deseas actualizar el archivo .env con estos datos? (s/n)${NC}"
    read -r RESPUESTA
    
    if [ "$RESPUESTA" = "s" ] || [ "$RESPUESTA" = "S" ]; then
      # Actualizar .env
      sed -i '' "s/INSTAGRAM_PAGE_ID=.*/INSTAGRAM_PAGE_ID=${LAST_IG_ID}/" .env
      echo -e "${GREEN}✓ Archivo .env actualizado${NC}"
      echo ""
      echo -e "${YELLOW}Próximos pasos:${NC}"
      echo "1. Cargar variables: ${GREEN}export \$(cat .env | xargs)${NC}"
      echo "2. Reiniciar la aplicación"
      echo "3. ¡Probar envío de mensajes!"
    fi
  else
    echo -e "${YELLOW}Tienes múltiples cuentas vinculadas.${NC}"
    echo "Edita manualmente el archivo .env y elige cuál usar:"
    echo ""
    echo "${BLUE}nano .env${NC}"
  fi
fi

echo ""

