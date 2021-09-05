export const loadStocks = symbols => ({
    type: 'LOAD_STOCKS',
    symbols
});

export const sortStocks = sortBy => ({
  type: 'SORT_STOCKS',
  sortBy
});

export const stocksLoaded = (stocks, symbols) => ({
  type: 'STOCKS_LOADED',
  stocks,
  symbols
});

export const symbolsChanged = (stocks, symbols, changeType = 'EDIT') => ({
  type: 'SYMBOLS_CHANGED',
  stocks,
  symbols,
  changeType
});

export const dashboardLoaded = () => ({
  type: 'DASHBOARD_LOADED'
});

export const editStart = () => ({
  type: 'EDIT_START'
});

export const addStock = () => ({
  type: 'ADD_STOCK'
});

export const symbolsLoaded = (allSymbols) => ({
  type: 'SYMBOLS_LOADED',
  allSymbols
});

export const stockAdded = (symbol) => ({
  type: 'STOCK_ADDED',
  symbol
});

export const priceDisplayChanged = () => ({
  type: 'PRICE_DISPLAY_CHANGED'
});
