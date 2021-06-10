import React, { Component } from 'react';
import './SiteHeader.scss';

class SiteHeader extends Component { 
    render() {
        return (
            <header className="site-header">
                {this.props.children}
            </header>
        );
    }
}

export default SiteHeader;