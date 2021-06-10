import React, { Component } from 'react';
import StockDashboardLineItem from '../stock-dashboard-line-item/StockDashboardLineItem';
import AddStock from '../add-stock/AddStock';
import { timer } from 'rxjs';
import { connect } from 'react-redux';
import {
  loadStocks, sortStocks, symbolsChanged, priceDisplayChanged,
  dashboardLoaded, editStart, addStock, stockAdded
}
  from '../../store/actions/stockActions';
import { Button, Dimmer, Loader, Dropdown, Modal } from 'semantic-ui-react';
import './StockDashboard.scss';

class StockDashboard extends Component {
  sortOptions = [
    { text: 'Change [Asc]', value: 'change' },
    { text: 'Change % [Asc]', value: 'changePercent' },
    { text: 'Symbol [Asc]', value: 'symbol' },
    { text: 'Company Name [Asc]', value: 'companyName' },
    { text: 'Market Cap. [Asc]', value: 'marketCap' },
    { text: 'Change [Desc]', value: '-change' },
    { text: 'Change % [Desc]', value: '-changePercent' },
    { text: 'Symbol [Desc]', value: '-symbol' },
    { text: 'Company Name [Desc]', value: '-companyName' },
    { text: 'Market Cap. [Desc]', value: '-marketCap' }
  ]

  onAddClose() {
    this.props.stockAdded(null);
  }

  doneEditing() {
    const stocks = this.props.stocks.filter(s => !s.isRemoved);
    const symbols = stocks.map(s => s.symbol);
    this.props.symbolsChanged(stocks, symbols);
  }

  render() {
    const tStyle = { width: '20%' };
    return (
      <div className='dashboard'>
        <Dimmer inverted active={this.props.isLoading}>
          <Loader>Loading ...</Loader>
        </Dimmer>
        {/* <Dropdown placeholder='Sort By'
          onChange={this.sortItems.bind(this)}
          compact selection options={this.sortOptions} style={tStyle} /> */}
        {/* <Modal open={this.props.addStockOpen}
          closeOnEscape={true}
          closeOnDimmerClick={true}>
          <Modal.Header>TEST</Modal.Header>
          <Modal.Content>
          Hey There

          </Modal.Content>
          <Modal.Actions>
            <Button
              content='Close' />
          </Modal.Actions>
        </Modal> */}
        <ul className='stocks-list'>
          {this.props.stocks.map(s =>
            <StockDashboardLineItem
              isEditMode={this.props.isEditMode}
              stock={s}
              symbolModalOpen={false}
              displayIndex={this.props.displayIndex}
              displayFields={this.props.displayFields}
              displayChanged={this.props.priceDisplayChanged}
              key={s.symbol}>
            </StockDashboardLineItem>
          )}
        </ul>
        <AddStock
          allSymbols={this.props.allSymbols}
          open={this.props.addStockOpen}
          onClose={this.onAddClose.bind(this)}
          onSave={this.props.stockAdded}>
        </AddStock>
        {!this.props.isEditMode ? (
          <footer className='toolbar'>
            <Button onClick={this.props.addStock}
              color='vk'
              size='large'
              content='Add Stock' />
            {/* <Button onClick={this.props.editStart}
              color='orange'
              size='large'
              content='Edit Stocks' /> */}
          </footer>
        ) : (
            <footer className='toolbar'>
              <Button onClick={this.doneEditing.bind(this)}
                color='green'
                size='large'
                content='Done' />
            </footer>
          )}
      </div>
    );
  }

  componentWillMount() {
    this.props.dashboardLoaded();
  }

  componentDidMount() {
    const interval = 3000;
    const timer$ = timer(interval, interval)
    this.timerSubs = timer$
      .subscribe(() =>
        this.props.loadStockQuotes(this.props.symbols)
      );
  }

  componentWillUnmount() {
    if (this.timerSubs) {
      this.timerSubs.unsubscribe();
    }
  }

  sortItems(_, target) {
    this.props.sortStocks(target.value);
  }
}

const mapDispatchToProps = dispatch => ({
  loadStockQuotes: symbols => dispatch(loadStocks(symbols)),
  sortStocks: sortBy => dispatch(sortStocks(sortBy)),
  dashboardLoaded: () => dispatch(dashboardLoaded()),
  symbolsChanged: (stocks, symbols) => dispatch(symbolsChanged(stocks, symbols)),
  editStart: () => dispatch(editStart()),
  addStock: () => dispatch(addStock()),
  stockAdded: (symbol) => dispatch(stockAdded(symbol)),
  priceDisplayChanged: () => dispatch(priceDisplayChanged())
});

const mapStateToProps = state => {
  return { ...state.dashboard };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StockDashboard);
