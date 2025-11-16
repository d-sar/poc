import axios from 'axios';

const API_BASE_URL = 'http://localhost:8888/';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Intercepteur de requête amélioré
api.interceptors.request.use(
  (config) => {
    console.log('🚀 Envoi requête:', config.method?.toUpperCase(), config.baseURL + config.url);
    console.log('📦 Données:', config.data);
    return config;
  },
  (error) => {
    console.error('❌ Erreur configuration requête:', error);
    return Promise.reject(error);
  }
);

// Intercepteur de réponse amélioré
api.interceptors.response.use(
  (response) => {
    console.log('✅ Réponse reçue:', response.status, response.data);
    return response;
  },
  (error) => {
    if (error.code === 'ERR_NETWORK') {
      console.error('🌐 Erreur réseau - Vérifiez que le backend est démarré sur localhost:8888');
      console.error('Détails:', error.message);
    } else if (error.response) {
      // Le serveur a répondu avec un code d'erreur
      console.error('❌ Erreur HTTP:', error.response.status, error.response.data);
    } else if (error.request) {
      // La requête a été faite mais aucune réponse reçue
      console.error('📡 Aucune réponse du serveur');
    } else {
      // Quelque chose s'est mal passé lors de la configuration
      console.error('⚙️ Erreur de configuration:', error.message);
    }
    return Promise.reject(error);
  }
);

// Service Bénéficiaires
export const beneficiaireService = {
  getAll: () => api.get('beneficiaire-service/beneficiaires'),
  getById: (id) => api.get(`beneficiaire-service/beneficiaires/${id}`),
  create: (beneficiaire) => api.post('beneficiaire-service/beneficiaires', beneficiaire),
  update: (id, beneficiaire) => api.put(`beneficiaire-service/beneficiaires/${id}`, beneficiaire),
  delete: (id) => api.delete(`beneficiaire-service/beneficiaires/${id}`),
  checkExists: (id) => api.get(`beneficiaire-service/beneficiaires/${id}/exists`),
  getByRib: (rib) => api.get(`beneficiaire-service/beneficiaires/rib/${rib}`),
};

// Service Virements
export const virementService = {
  getAll: () => api.get('/virements'),
  getById: (id) => api.get(`/virements/${id}`),
  getWithDetails: (id) => api.get(`/virements/${id}/details`),
  create: (virement) => api.post('/virements', virement),
  updateStatus: (id, nouveauStatut) => 
    api.put(`/virements/${id}/statut?nouveauStatut=${nouveauStatut}`),
  cancel: (id) => api.post(`/virements/${id}/annuler`),
  getByBeneficiaire: (beneficiaireId) => 
    api.get(`/virements/beneficiaire/${beneficiaireId}`),
  getByRibSource: (ribSource) => 
    api.get(`/virements/source/${ribSource}`),
  getByStatus: (statut) => 
    api.get(`/virements/statut/${statut}`),
  getStats: (ribSource, startDate, endDate) =>
    api.get(`/virements/stats/total?ribSource=${ribSource}&startDate=${startDate}&endDate=${endDate}`),
};

// Service ChatBot
export const chatBotService = {
  askQuestion: (question) => 
    api.post('/chatbot/ask', { question }),
  getServices: () => 
    api.get('/chatbot/services'),
};

export default api;