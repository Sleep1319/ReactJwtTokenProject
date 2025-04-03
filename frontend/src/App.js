import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { UserProvider } from "./context/UserContext";
import AppRoutes from './routes/Routes';
import AppNavbar from './layout/AppNavbar';


function App() {

  return (
    <UserProvider>
      <Router>
        <AppNavbar />
        <AppRoutes />
      </Router>
    </UserProvider>
  );
}

export default App;
