import React, { Component } from 'react';
import SiteHeader from './components/site-header/SiteHeader';
import SiteContent from './components/site-content/SiteContent';
import StockDashboard from './components/stock-dashboard/StockDashboard';
import './App.scss';

class App extends Component {
  render() {
    return (
      <div id="app-container">
        <SiteHeader>
          <h3>Stock Dashboard Verion 2</h3>
        </SiteHeader>
        <SiteContent>
          <StockDashboard>
          </StockDashboard>
        </SiteContent>
      </div>
    );
  }
}

export default App;