# TO RUN APP IN ANDROID STUDIO IDE

Click run 'app' using the preferred device by the user, be it a simulator or a connected phone

## Implementation

- Every proposed view in the design document is implemented, with extra views like editing a trip
- Some views dont follow 100% fidelity due to complications in how its seen in-app
- Trip cards in home are non-functional and will take to the "trips" tab instead
- Dark mode is functional (it's not preserved after restarting the app)

## Functions

- Trips can be created, edited and deleted freely (no confirmation to delete for now, misclicks may happen)
- Trips are represented in ui, although no image of the location or valid real locations are displayed (this includes at the moment of creation)
- Filtering is not yet implemented
- Map is non functional
- As seen in the code, toasts are shown when saving with DataStore is succesful or it fails
  - This saving includes: Trips, trip editing, trip deletion, adding expenses, adding activities, adding packing items
