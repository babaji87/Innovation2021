export const searchStocks = (input, allSymbols) => ({
  type: 'SEARCH_STOCKS',
  input,
  allSymbols
});

export const selectStock = stock => ({
  type: 'SELECT_STOCK',
  stock
});
