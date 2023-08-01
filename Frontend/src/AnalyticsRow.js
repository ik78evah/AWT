import React, {Component} from "react";
import DetailView from "./DetailView";
import {v4 as uuidv4} from 'uuid';

class AnalyticsRow extends Component {

   componentDidMount() {
      let modal = document.getElementById(`modal${this.props.item.identifier}`);
      let btn = document.getElementById(`detail${this.props.item.identifier}`);
      let content = document.getElementById(`content${this.props.item.identifier}`);

      btn.onclick = function () {
         modal.style.display = "block";
      }

      window.onclick = function (event) {
         if (event.target === content) {
            modal.style.display = "none";
         }
      }

      content.onclick = function (event) {
         modal.style.display = "none";
      }
   }

   render() {
      const item = this.props.item
      const params = this.props.item.parameters
      return (
         <tr className="bg-white border-b hover:bg-gray-50">
            <td
               className="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap">
               {this.props.item.name}
            </td>
            <td className="px-6 py-4 text-sm text-gray-500 whitespace-nowrap">
               {this.props.item.description}
            </td>
            <td className="px-6 py-4 text-sm text-gray-500 whitespace-nowrap">
               {this.props.item.count}
            </td>
            <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
               {this.props.item.source}
            </td>
             <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                 {this.props.item.source === "API" ? "mBaaS" : this.props.item.project }
             </td>
            <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
               {this.props.item.creationDate}
            </td>
            <td className="px-6 py-4" id={`detail${this.props.item.identifier}`}>
               <button className="text-sm font-bold text-sky-500 rounded">
                   Details
               </button>
            </td>

            <td>
               <div className="fixed hidden inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full" id={`modal${this.props.item.identifier}`}>
                  <div className="h-full items-center justify-center flex flex-col px-5" id={`content${this.props.item.identifier}`}>
                     <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-fit py-2 sm:px-6 lg:px-8 space-y-4">
                           <h2 className={"px-6 py-3 font-bold text-sky-500 whitespace-nowrap bg-white sm:rounded-lg"}>
                              Details
                           </h2>
                           <div className="overflow-hidden shadow-md sm:rounded-lg">
                              <table className="table-auto min-w-full">
                                 <tbody>
                                 {Object.keys(item).filter(key => key !== "parameters").map(key =>
                                    <DetailView key={key} title={this.capitalized(key)} value={item[key]}/>
                                 )}
                                 <tr className={`flex items-center place-content-between bg-white border-b hover:bg-gray-50 min-w-fit`}>
                                    <td className="px-6 py-4 text-sm font-bold text-sky-500 whitespace-nowrap">
                                       CUSTOM PARAMETERS ({Object.keys(params).length})
                                    </td>
                                 </tr>
                                 {Object.keys(params).map(key =>
                                    <DetailView key={uuidv4()} title={this.capitalized(key)} value={params[key]}/>
                                 )}
                                 </tbody>
                              </table>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </td>
         </tr>
      );
   }

   capitalized(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
   }

   getRandomInt(max) {
      return Math.floor(Math.random() * max);
   }
}

export default AnalyticsRow;