// delete-tests.js
const fs = require('fs');
const path = require('path');

const rootDir = path.resolve(__dirname, 'src'); // modifie si besoin le dossier racine

function deleteTestFiles(dir) {
  fs.readdir(dir, { withFileTypes: true }, (err, files) => {
    if (err) {
      console.error(`Erreur lecture dossier ${dir}:`, err);
      return;
    }
    files.forEach(file => {
      const fullPath = path.join(dir, file.name);
      if (file.isDirectory()) {
        deleteTestFiles(fullPath); // récursif dans sous-dossiers
      } else if (file.name.endsWith('.spec.ts') || file.name.endsWith('.test.ts')) {
        fs.unlink(fullPath, err => {
          if (err) {
            console.error(`Erreur suppression fichier ${fullPath}:`, err);
          } else {
            console.log(`Supprimé : ${fullPath}`);
          }
        });
      }
    });
  });
}

deleteTestFiles(rootDir);
