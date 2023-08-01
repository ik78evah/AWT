import React from "react";

export default function NavItem({ id, isActive, children }) {
   return (
         <button id={id} className={`block px-3 py-2 rounded-md ${isActive ? 'bg-sky-500 text-white' : 'bg-gray-200'}`}>
            {children}
         </button>
   )
}