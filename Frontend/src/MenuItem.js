import React from "react";

export default function MenuItem({ href, title, isActive }) {
   return (
         <h1 className={`block py-2 pl-3 pr-4 ${isActive ? 'text-sky-500' : 'text-gray-700'} border-b text-medium border-gray-100 hover:bg-gray-50 md:hover:bg-transparent md:border-0 md:hover:text-sky-500 md:p-0`}>
            {title}
         </h1>
   )
}