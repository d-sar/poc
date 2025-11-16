import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import BeneficiairesPage from './pages/BeneficiairesPage';
import VirementsPage from './pages/VirementsPage';
import ChatBotPage from './pages/ChatBotPage';
import { BeneficiaireProvider } from './context/BeneficiaireContext';
import { VirementProvider } from './context/VirementContext';
import './App.css';

function App() {
  return (
    <BeneficiaireProvider>
      <VirementProvider>
        <Router>
          <div className="min-h-screen bg-gray-50">
            <Navbar />
            <main className="container mx-auto px-4 py-8">
              <Routes>
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/beneficiaires" element={<BeneficiairesPage />} />
                <Route path="/virements" element={<VirementsPage />} />
                <Route path="/chatbot" element={<ChatBotPage />} />
              </Routes>
            </main>
          </div>
        </Router>
      </VirementProvider>
    </BeneficiaireProvider>
  );
}

export default App;