import StockService from '../../services/StockService';

const displayFields = [
  'change',
  'changePercentFormatted',
  'marketCapFormatted'
];

const defaultState = {
  symbols: '',
  stocks: [],
  isEditMode: false,
  allSymbols: [],
  addStockOpen: false,
  isLoading: true,
  displayIndex: 1,
  displayFields
};

export default (state = defaultState, action) => {
  switch (action.type) {
    case 'STOCKS_LOADED': {
      return Object.assign({...state}, {
        stocks: action.stocks,
        symbols: action.symbols,
        isLoading: false
      });
    }
    case 'SORT_STOCKS': {
      return Object.assign({...state}, {
        stocks: [...state.stocks.sort(StockService.ItemSort(action.sortBy))]
      });
    }
    case 'SYMBOLS_CHANGED': {
      return Object.assign({...state}, {
        isEditMode: false,
        stocks: action.stocks || state.stocks,
        isLoading: action.changeType === 'ADD' ? true: false
      });
    }
    case 'EDIT_START': {
      return Object.assign({...state}, {
        isEditMode: true
      });
    }
    case 'ADD_STOCK': {
      return Object.assign({...state}, {
        addStockOpen: true
      });
    }
    case 'SYMBOLS_LOADED': {
      return Object.assign({...state}, {
        addStockOpen: true,
        allSymbols: action.allSymbols || []
      });
    }
    case 'STOCK_ADDED': {
      return Object.assign({...state}, {
        addStockOpen: false
      });
    }
    case 'PRICE_DISPLAY_CHANGED': {
      let newIndex = state.displayIndex + 1;
      if (newIndex === displayFields.length) {
        newIndex = 0;
      }
      return Object.assign({...state}, {
        displayIndex: newIndex
      });
    }
    default: {
      return state;
    }
  }
}
