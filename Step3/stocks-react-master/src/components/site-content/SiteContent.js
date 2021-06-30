import React, { Component } from 'react';
import './SiteContent.scss';

class SiteContent extends Component {
    render() {
        return (
            <section className="site-content">
               {this.props.children}
            </section>
        );
    }
}

export default SiteContent;