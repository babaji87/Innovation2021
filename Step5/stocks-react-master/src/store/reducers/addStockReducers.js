const defaultState = {
  selectedStock: null,
  matchingStocks: []
};

export default (state = defaultState, action) => {
  switch (action.type) {
    case 'SEARCH_STOCKS': {
      const { input } = action;
      let matching = [];
      if(input) {
        matching = action.allSymbols.filter(s => input === s.symbol);
        if (matching.length === 0) {
          const regx = new RegExp(input, 'ig');
          matching = action.allSymbols.filter(s => regx.test(s.name));
        };
      }
      return Object.assign({...state}, {
        matchingStocks: [...matching]
      });
    }
    case 'SELECT_STOCK': {
      return Object.assign({...state}, {
        selectedStock: action.stock
      });
    }
    case 'STOCK_ADDED': {
      return Object.assign({...state}, {
        matchingStocks: [],
        selectedStock: null,
        addStockOpen: false
      });
    }
    default: {
      return state;
    }
  }
}
