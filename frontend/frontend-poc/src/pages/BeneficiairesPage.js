import React, { useEffect, useState } from 'react';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import { useBeneficiaire } from '../context/BeneficiaireContext';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import BeneficiaireForm from '../components/BeneficiaireForm';

const BeneficiairesPage = () => {
  const { beneficiaires, loading, error, fetchBeneficiaires, deleteBeneficiaire } = useBeneficiaire();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingBeneficiaire, setEditingBeneficiaire] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchBeneficiaires();
  }, [fetchBeneficiaires]);

  const filteredBeneficiaires = beneficiaires.filter(beneficiaire =>
    beneficiaire.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    beneficiaire.prenom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    beneficiaire.rib.includes(searchTerm)
  );

  const handleEdit = (beneficiaire) => {
    setEditingBeneficiaire(beneficiaire);
    setIsModalOpen(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce bénéficiaire ?')) {
      try {
        await deleteBeneficiaire(id);
      } catch (err) {
        // Error is handled in context
      }
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingBeneficiaire(null);
  };

  const handleCreate = () => {
    setEditingBeneficiaire(null);
    setIsModalOpen(true);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Bénéficiaires</h1>
        <button
          onClick={handleCreate}
          className="bg-primary-600 text-white px-4 py-2 rounded-lg hover:bg-primary-700 transition-colors flex items-center space-x-2"
        >
          <Plus className="h-4 w-4" />
          <span>Nouveau Bénéficiaire</span>
        </button>
      </div>

      {/* Recherche */}
      <div className="bg-white rounded-lg shadow-sm border p-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
          <input
            type="text"
            placeholder="Rechercher un bénéficiaire..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          />
        </div>
      </div>

      {/* Liste des bénéficiaires */}
      <div className="bg-white rounded-lg shadow-sm border">
        {loading ? (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner />
          </div>
        ) : error ? (
          <div className="text-center py-8 text-red-600">
            {error}
          </div>
        ) : filteredBeneficiaires.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-gray-500">Aucun bénéficiaire trouvé</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nom & Prénom
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    RIB
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Type
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredBeneficiaires.map((beneficiaire) => (
                  <tr key={beneficiaire.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {beneficiaire.nom} {beneficiaire.prenom}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900 font-mono">
                        {beneficiaire.rib}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        beneficiaire.type === 'PHYSIQUE'
                          ? 'bg-blue-100 text-blue-800'
                          : 'bg-green-100 text-green-800'
                      }`}>
                        {beneficiaire.type}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex space-x-2">
                        <button
                          onClick={() => handleEdit(beneficiaire)}
                          className="text-blue-600 hover:text-blue-900 transition-colors"
                        >
                          <Edit className="h-4 w-4" />
                        </button>
                        <button
                          onClick={() => handleDelete(beneficiaire.id)}
                          className="text-red-600 hover:text-red-900 transition-colors"
                        >
                          <Trash2 className="h-4 w-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal de création/édition */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingBeneficiaire ? 'Modifier le bénéficiaire' : 'Nouveau bénéficiaire'}
      >
        <BeneficiaireForm
          beneficiaire={editingBeneficiaire}
          onSuccess={handleCloseModal}
          onCancel={handleCloseModal}
        />
      </Modal>
    </div>
  );
};

export default BeneficiairesPage;