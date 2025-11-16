/** 
 * Fichier de configuration principal de Tailwind CSS
 * Il permet de personnaliser et d'étendre le design du projet React.
 */

module.exports = {
  // Indique à Tailwind où chercher les fichiers qui contiennent du code HTML/JSX
  // pour générer uniquement les classes CSS utilisées (optimisation)
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],

  theme: {
    extend: {
      // Permet d’ajouter ou de modifier des couleurs personnalisées dans le thème
      colors: {
        primary: {
          50: '#eff6ff',  // teinte très claire
          100: '#dbeafe', // teinte claire
          500: '#3b82f6', // couleur principale
          600: '#2563eb', // teinte plus foncée
          700: '#1d4ed8', // teinte encore plus foncée
        }
      }
    },
  },

  // Permet d’ajouter des plugins Tailwind (ex: formulaires, typographie, etc.)
  plugins: [],
}
