import axios from 'axios';

class StockService {
  //apiUrl = 'https://cloud.iexapis.com/stable/tops?token=pk_53f96e249be3442d803886bb59504119';
  //apiUrl = 'https://cloud.iexapis.com/stable/stock/AMZN/quote?token=pk_53f96e249be3442d803886bb59504119';

  SI_PREFIXES = [
    { value: 1, symbol: '' },
    { value: 1e3, symbol: 'k' },
    { value: 1e6, symbol: 'M' },
    { value: 1e9, symbol: 'B' },
    { value: 1e12, symbol: 'T' },
    { value: 1e15, symbol: 'P' },
    { value: 1e18, symbol: 'E' },
  ];

  round(value, digits = 2) {
    if (typeof value === 'number' && !isNaN(value)) {
      value = value * Math.pow(10, digits);
      value = Math.round(value);
      value = value / Math.pow(10, digits);
      return value;
    }
    return value;
  }

  abbreviateNumber(value) {
    if (value === 0) return '0';
    const tier = this.SI_PREFIXES.filter((n) => value >= n.value).pop();
    const numberFixed = (value / tier.value).toFixed(1);
    return `${numberFixed}${tier.symbol}`;
  }

  static ItemSort(property) {
    let sortOrder = 1;
    if (property[0] === "-") {
      sortOrder = -1;
      property = property.substr(1);
    }
    return function (a, b) {
      var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
      return result * sortOrder;
    }
  }


  /**
   *
   * @param {string} symbols Comma separate list of Symbols
   */
  getStocks(symbols) {
    axios.defaults.headers.get['Access-Control-Allow-Origin'] = '*';
    axios.defaults.headers.get['Access-Control-Allow-Methods'] = 'GET, POST, PATCH, PUT, DELETE, OPTIONS';
    axios.defaults.headers.get['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token';
    
    return axios
    .get(`http://10.1.207.98:8700/api/v1/stocks`)
    .then(results => {
      console.log(results);
      return results.data.map(d => {
        d.changePercentFormatted = 0;
        d.marketCapFormatted=0;
        if(d.changePercent !== null)
        d.changePercentFormatted = this.round(d.changePercent * 100)+ '%';
        if(d.marketCap !== null)
        d.marketCapFormatted = this.abbreviateNumber(d.marketCap);
        return d;
      });
    });
  }


  /**
   * Gets a list of all symbols
   */
  getAllSymbols() {
    axios.defaults.headers.get['Access-Control-Allow-Origin'] = '*';
    return axios.get(`http://10.1.207.98:8700/transformers/all-stocks`)
                .then(results => {
                    return results.data;
                });
  }

  getSavedSymbols() {
    const defaultSymbols = 'aapl,fb,msft,amzn,goog';
    return window.localStorage.getItem('symbols') || defaultSymbols;
  }

  saveSymbols(symbols) {
    window.localStorage.setItem('symbols', symbols);
  }

}


export default StockService;
