# Portfolio site (Astro)

Frontend public du portfolio, rendu avec Astro et branché sur le backend Quarkus.

## Développement

```bash
cd /home/epita/Documents/portfolio/site
npm install
npm run dev
```

Par défaut, le site appelle le backend sur `http://localhost:8080/portfolio`.

Tu peux changer l’URL avec :

```bash
PORTFOLIO_API_URL=http://localhost:8080 npm run dev
```

## Build

```bash
npm run build
```

