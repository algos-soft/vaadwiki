import {PolymerElement,html} from '@polymer/polymer/polymer-element.js';

class ExampleTemplate extends PolymerElement {

    static get template() {
        return html`
            <span>[[message]]Forse non c'è nessun messaggio</span>`;
    }

    static get is() {
          return 'example-template';
    }
};

customElements.define(ExampleTemplate.is, ExampleTemplate);
