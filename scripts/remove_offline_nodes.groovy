// Removes offline nodes from AzureVMAgents that weren't taken offline for user reasons

import com.microsoft.azure.vmagent.AzureVMAgent 

def nodes = Jenkins.instance.getNodes()

nodes.each { node ->
  if (node instanceof AzureVMAgent) {
    if (node.getComputer() != null) {
      if (node.getComputer().isOffline()) {
        def cause = node.getComputer().getOfflineCause()
        if (node.getComputer().isTemporarilyOffline() && !(cause instanceof hudson.slaves.OfflineCause.UserCause))
          println "Removing " + node.getComputer().getName() + " " + (cause != null ? cause.getClass() : cause)
          Jenkins.instance.removeNode(node)
        } else if (node.getComputer().isOffline() && cause instanceof hudson.slaves.OfflineCause.ChannelTermination) {
          println "Removing " + node.getComputer().getName() + " " + (cause != null ? cause.getClass() : cause)
          Jenkins.instance.removeNode(node)
        }
      }
    }
}

return true