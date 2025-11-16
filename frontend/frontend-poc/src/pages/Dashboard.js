import React from 'react';
import { Users, Send, MessageCircle, TrendingUp } from 'lucide-react';

const Dashboard = () => {
  const statCards = [
    {
      title: 'Bénéficiaires',
      value: '0',
      icon: Users,
      color: 'blue'
    },
    {
      title: 'Virements Total',
      value: '0',
      icon: Send,
      color: 'green'
    },
    {
      title: 'Montant Total',
      value: '0 €',
      icon: TrendingUp,
      color: 'purple'
    },
    {
      title: 'Virements Validés',
      value: '0',
      icon: MessageCircle,
      color: 'orange'
    }
  ];

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Tableau de Bord</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statCards.map((card, index) => {
          const Icon = card.icon;
          const colorClasses = {
            blue: 'bg-blue-50 text-blue-600',
            green: 'bg-green-50 text-green-600',
            purple: 'bg-purple-50 text-purple-600',
            orange: 'bg-orange-50 text-orange-600'
          };

          return (
            <div key={index} className="bg-white rounded-lg shadow-sm border p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">{card.title}</p>
                  <p className="text-2xl font-bold text-gray-800 mt-2">{card.value}</p>
                </div>
                <div className={`p-3 rounded-full ${colorClasses[card.color]}`}>
                  <Icon className="h-6 w-6" />
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Bienvenue dans BanqueApp</h2>
        <p className="text-gray-600">
          Utilisez la navigation pour gérer vos bénéficiaires et virements.
        </p>
      </div>
    </div>
  );
};

export default Dashboard;