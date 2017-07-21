package org.starlightfinancial.deductiongateway.faces;

public class BaseBean {

    public int[] getIntParameters(String key) {
//        String[] parameter = FacesContext.getCurrentInstance()
//                .getExternalContext().getRequestParameterValuesMap().get(key);
//
//        if (parameter == null) {
//            return null;
//        }
//
//        int[] intParameter = new int[parameter.length];
//
//        for (int i = 0; i < intParameter.length; ++i) {
//            try {
//                intParameter[i] = Integer.parseInt(parameter[i]);
//            } catch (Exception e) {
//                // e.printStackTrace();
//                intParameter[i] = -1;
//            }
//        }
//
        return new int[]{};
    }

    public void showMessage(int type, String message) {
//
//        if (message != null && !message.equals("")) {
//            if (type == 0) {// global
//                FacesContext context = FacesContext.getCurrentInstance();
//                context.addMessage("global", new FacesMessage("", message));
//            } else {
//                FacesContext context = FacesContext.getCurrentInstance();
//                context.addMessage(null, new FacesMessage("", message));
//            }
//
//        }
    }

}
