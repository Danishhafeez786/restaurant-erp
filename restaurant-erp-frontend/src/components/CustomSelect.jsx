import { Listbox } from "@headlessui/react";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/react/24/outline";

export default function CustomSelect({ options, value, onChange }) {
  const selected = options.find((item) => item.value === value) || options[0];

  return (
    <Listbox value={selected} onChange={onChange}>
      <div className="relative w-full">
        <Listbox.Button
          className="
            flex w-full items-center justify-between
            bg-transparent text-left text-sm
            focus:outline-none
          "
        >
          <span>{selected.label}</span>

          <ChevronUpDownIcon className="h-5 w-5 text-gray-500" />
        </Listbox.Button>

        <Listbox.Options
          className="
            absolute left-0 top-full z-50 mt-2
            w-full overflow-hidden
            rounded-lg border border-gray-200
            bg-white shadow-lg
            focus:outline-none
          "
        >
          {options.map((item) => (
            <Listbox.Option
              key={item.value}
              value={item}
              className={({ active }) =>
                `flex cursor-pointer items-center justify-between px-4 py-2 text-sm ${
                  active ? "bg-gray-100" : ""
                }`
              }
            >
              {({ selected }) => (
                <>
                  <span className={selected ? "font-semibold" : ""}>
                    {item.label}
                  </span>

                  {selected && <CheckIcon className="h-4 w-4 text-green-600" />}
                </>
              )}
            </Listbox.Option>
          ))}
        </Listbox.Options>
      </div>
    </Listbox>
  );
}
